from tkinter import *
from tkinter import messagebox, filedialog
from Modules import PublicModules as libs
from Modules import LSTM_Config as cf
import cv2
import threading
from PIL import Image
from PIL import ImageTk

from Modules.MyThreading import MyThreadingVideo

WINDOWS_WIDTH = int(1280 * 0.6)
WINDOWS_HEIGHT = int(720 * 0.6)
URL_VIDEO = 'FileInput/006.avi'
IS_USING_WEBCAM = False
CURSOR_DF = 'hand2'
CURSOR_NO = 'spider'


class EntryWithPlaceholder(Entry):
    def __init__(self, master=None, placeholder="PLACEHOLDER", color='grey'):
        super().__init__(master)

        self.placeholder = placeholder
        self.placeholder_color = color
        self.default_fg_color = self['fg']

        self.bind("<FocusIn>", self.foc_in)
        self.bind("<FocusOut>", self.foc_out)

        self.put_placeholder()

    def put_placeholder(self):
        self.insert(0, self.placeholder)
        self['fg'] = self.placeholder_color

    def foc_in(self, *args):
        if self['fg'] == self.placeholder_color:
            self.delete('0', 'end')
            self['fg'] = self.default_fg_color

    def foc_out(self, *args):
        if not self.get():
            self.put_placeholder()


class ChoseSourceWindow:
    def __init__(self, master):
        self.isUsingIpWebcam = IntVar()
        self.valSource = StringVar()
        self.master = master
        self.master.minsize(500, 100)
        self.frame = Frame(self.master)
        # self.master.grab_set()
        libs.fun_makeCenter(self.master)
        self.DIALOG_OK = False
        self.RETURN_RESULT = 'NULL'
        self.iconCheck= PhotoImage(file='FileInput/Icons/ic_check2.png').subsample(3, 3)
        self.iconMp4 = PhotoImage(file='FileInput/Icons/ic_check2.png').subsample(3, 3)

        # goi sau cung nhe
        self.fun_initComponent()

    def fun_initComponent(self):
        self.frame.grid(row=0, column=0, sticky='nsew')
        self.master.grid_columnconfigure(0, weight=1)
        self.master.grid_rowconfigure(0, weight=1)

        # frame 1
        self.frame1 = Frame(self.frame, bg='#95deff', padx=10, pady=10)
        self.frame2 = Frame(self.frame, bg='#c1ffe5', padx=10, pady=10)
        self.frame3 = Frame(self.frame, bg='#f7b5c7', padx=10, pady=10)

        self.frame1.grid(row=0, column=0, sticky='nsew')
        self.frame2.grid(row=1, column=0, sticky='nsew')
        self.frame3.grid(row=2, column=0, sticky='nsew')

        self.frame.grid_columnconfigure(0, weight=1)
        self.frame.grid_rowconfigure(0, weight=1)
        self.frame.grid_rowconfigure(1, weight=1)
        self.frame.grid_rowconfigure(2, weight=1)

        self.checkDir = Checkbutton(self.frame1, text='VIDEO FROM DISK...',
                                    variable=self.isUsingIpWebcam, command=self.fun_CheckIsUsingCamChange,
                                    padx=10, pady=10,
                                    font=('Helvetica', 18, 'bold'),
                                    cursor=CURSOR_DF
                                    )
        self.checkDir.grid(row=0, column=0, sticky='nsew')
        self.frame1.grid_rowconfigure(0, weight=1)
        self.frame1.grid_columnconfigure(0, weight=1)

        self.tbSource = EntryWithPlaceholder(self.frame2, 'IP WEBCAM EXAMPLE: 192.168.1.1')
        self.tbSource.grid(row=0, column=0, sticky='nsew')

        self.btnSource = Button(self.frame2,
                                command=self.btnGetPathFromSourceClicked, cursor=CURSOR_DF,
                                image= self.iconCheck,
                                compound= CENTER,
                                bg='#c1ffe5'
                                )
        self.btnSource.grid(row=0, column=1, sticky='nsew')

        self.frame2.grid_columnconfigure(0, weight=9)
        self.frame2.grid_columnconfigure(1, weight=1)
        self.frame2.grid_rowconfigure(0, weight=1)

        self.btnOk = Button(self.frame3, padx=10, pady=10, text='Load Video Clip'
                            , command=self.btnLoadVideoClicked,
                            state='disable',
                            cursor=CURSOR_NO
                            )
        self.btnOk.grid(row=0, column=0, sticky='nsew')
        self.frame3.grid_columnconfigure(0, weight=1)
        self.frame3.grid_rowconfigure(0, weight=1)

    def fun_CheckIsUsingCamChange(self):
        if self.isUsingIpWebcam.get() == 0:
            self.btnSource.config(image= self.iconCheck)
            holder = 'IP WEBCAM EXAMPLE: 192.168.1.1'
            self.checkDir.config(bg= 'white')
        else:
            self.btnSource.config(image= self.iconMp4)
            holder = 'EXAMPLE: C:/VIDEO/DETECTION.MP4'
            self.checkDir.config(bg= '#c1ffe5')

        self.fun_reloadHolderSource(source=holder)

    def fun_reloadHolderSource(self, source: str):
        self.tbSource.delete('0', 'end')
        self.tbSource.placeholder = source
        self.tbSource.put_placeholder()

    def fun_checkVideoFromSource(self, source: str):
        try:
            frames = libs.fun_getFramesOfVideo(path=source, count=20)
            messagebox.showinfo('Thong Bao', 'Check Video Load OK, Video Size: {0}'.format(frames[0].shape))
            return True
        except:
            messagebox.showerror('Thong Bao', 'Yeu Cau khong duoc chap nhan!')
            return False

    def fun_getURL_IPCam(self, ip: str):
        return '{0}{1}{2}'.format('http://', ip, ':8080/video')

    def btnLoadVideoClicked(self):
        if self.isUsingIpWebcam.get() == 0:
            self.RETURN_RESULT = self.fun_getURL_IPCam(ip=self.tbSource.get())
        self.DIALOG_OK = True
        self.master.destroy()

    def btnGetPathFromSourceClicked(self):
        if self.isUsingIpWebcam.get() == 0:
            url = self.fun_getURL_IPCam(ip=self.tbSource.get())
        else:
            self.RETURN_RESULT = filedialog.askopenfilename(initialdir="/", title="Select file",
                                                            filetypes=(("AVI files", "*.AVI"), ("MP4 files", "*.MP4"), ("ALL files", "*.*")))
            self.fun_reloadHolderSource(source=self.RETURN_RESULT)
            url = self.RETURN_RESULT

        isCheck = self.fun_checkVideoFromSource(source=url)
        if isCheck:
            self.btnOk.config(state='normal', cursor=CURSOR_DF)
        else:
            self.btnOk.config(state='disable', cursor=CURSOR_NO)

    def close_windows(self):
        self.master.destroy()


class MyApp:
    def __init__(self, title: str = 'GUI HUMAN''S VIOLENCE DETECTIONS'):
        self.URL_VIDEO = URL_VIDEO
        self.videoCap = None
        self.title = title
        self.root = Tk()
        self.root.title(string=title)
        self.arrACTION = []
        self.stopEvent = None
        self.IS_PAUSE = False

        self.containerTrai = None
        self.containerPhai = None

        self.root.minsize(width=WINDOWS_WIDTH, height=WINDOWS_HEIGHT)
        # libs.fun_makeCenter(self.root)
        libs.fun_makeMaximumSize(self.root)

        # Load model VGG16
        self.vgg16_model = None
        # self.vgg16_model.summary()

        # Load model LSTM
        self.lstm_model = None
        # self.lstm_model.summary()

        self.initComponent()

    def initComponent(self):
        #
        self.containerTrai = Frame(self.root, bg='white', padx=10, pady=10)
        self.containerPhai = Frame(self.root, bg='white', padx=10, pady=10)

        self.containerTrai.grid(row=0, column=0, sticky='nsew')
        self.containerPhai.grid(row=0, column=1, sticky='nsew')

        self.root.grid_columnconfigure(0, weight=1)
        self.root.grid_columnconfigure(1, weight=1)
        self.root.grid_rowconfigure(0, weight=1)

        # Container con cua trai
        self.containerChonNguonDuLieu = Frame(self.containerTrai, bg='#95deff', padx=10, pady=10)
        self.containerVideoCamera = Frame(self.containerTrai, bg='#c1ffe5', padx=10, pady=10)
        self.containerChucNang = Frame(self.containerTrai, bg='#f7b5c7', padx=10, pady=10)

        self.containerChonNguonDuLieu.grid(row=0, column=0, sticky='nsew')
        self.containerVideoCamera.grid(row=1, column=0, sticky='nsew')
        self.containerChucNang.grid(row=2, column=0, sticky='nsew')

        self.containerTrai.grid_columnconfigure(0, weight=1)
        self.containerTrai.grid_rowconfigure(0, weight=1)
        self.containerTrai.grid_rowconfigure(1, weight=8)
        self.containerTrai.grid_rowconfigure(2, weight=1)

        # giao dien cho button chon nguon du lieu
        iconChonNguonDuLieu = PhotoImage(file='FileInput/Icons/ic_dir.png')
        # Resizing image to fit on button
        iconChonNguonDuLieu = iconChonNguonDuLieu.subsample(1, 1)
        self.btnChonNguonDuLieu = Button(self.containerChonNguonDuLieu, padx=10,
                                         pady=10, text='INSERT VIDEO FROM SOURCE...',
                                         command=self.fun_chonNguonDuLieu,
                                         # bg='green',
                                         cursor=CURSOR_DF,
                                         font=('Helvetica', 18, 'bold'),
                                         image=iconChonNguonDuLieu,
                                         compound=LEFT
                                         )
        self.btnChonNguonDuLieu.image=iconChonNguonDuLieu
        self.btnChonNguonDuLieu.grid(row=0, column=0, sticky='nsew')

        # Giao dien cho nut load lai video
        iconTaiLaiVideo = PhotoImage(file='FileInput/Icons/ic_process.png')
        # Resizing image to fit on button
        iconTaiLaiVideo = iconTaiLaiVideo.subsample(1, 1)
        self.btnRefresh = Button(self.containerChonNguonDuLieu, padx=10,
                                 pady=10,
                                 # bg='green',
                                 # text='Tai lai video',
                                 command=self.fun_taiLaiVideo,
                                 state='disable',
                                 cursor=CURSOR_NO,
                                 image=iconTaiLaiVideo,
                                 compound=CENTER
                                 )
        self.btnRefresh.image= iconTaiLaiVideo
        self.btnRefresh.grid(row=0, column=1, sticky='nsew')

        # Giao dien cho nut ngat ket noi
        iconNgatKetNoi = PhotoImage(file='FileInput/Icons/ic_powerof.png')
        iconNgatKetNoi = iconNgatKetNoi.subsample(1, 1)
        self.btnDisconection = Button(self.containerChonNguonDuLieu, padx=10,
                             pady=10,
                             # bg='green',
                             # text='Ngat Ke Noi',
                             image=iconNgatKetNoi,
                             command=self.fun_ngatKetNoi,
                             cursor=CURSOR_DF,
                             compound=CENTER
                             )

        self.btnDisconection.imgage=iconNgatKetNoi
        self.btnDisconection.grid(row=0, column=2, sticky='nsew')

        self.containerChonNguonDuLieu.grid_columnconfigure(0, weight=8)
        self.containerChonNguonDuLieu.grid_columnconfigure(1, weight=1)
        self.containerChonNguonDuLieu.grid_columnconfigure(2, weight=1)
        self.containerChonNguonDuLieu.grid_rowconfigure(0, weight=1)

        # Container con cua phai
        self.containerPhanDoanBaoLuc = Frame(self.containerPhai, bg='#95deff', padx=10, pady=10)
        self.containerTongHopMoTaPhanDoanDanh = Frame(self.containerPhai, bg='#c1ffe5', padx=10, pady=10)

        self.containerPhanDoanBaoLuc.grid(row=0, column=0, sticky='nsew')
        self.containerTongHopMoTaPhanDoanDanh.grid(row=1, column=0, sticky='nsew')

        # Label hien thi loai bao luc gi
        self.lbKetQuaBaoLuc = Label(self.containerTongHopMoTaPhanDoanDanh,
                                    text='Khong Co Bao Luc', padx=10,
                                    pady=10,
                                    bg='white',
                                    font=('Helvetica', 18, 'bold')
                                    )
        self.lbKetQuaBaoLuc.grid(row=0, column=0, sticky='nsew')
        self.containerTongHopMoTaPhanDoanDanh.grid_rowconfigure(0, weight=1)
        self.containerTongHopMoTaPhanDoanDanh.grid_columnconfigure(0, weight=1)

        self.containerPhai.grid_rowconfigure(0, weight=9)
        self.containerPhai.grid_rowconfigure(1, weight=1)
        self.containerPhai.grid_columnconfigure(0, weight=1)

        # Container con cua ContainerVideoFrames
        self.lbVideoFrames = Label(self.containerVideoCamera, bg='white', padx=10, pady=10)
        self.lbVideoFrames.grid(row=0, column=0, sticky='nsew')

        self.containerVideoCamera.grid_rowconfigure(0, weight=1)
        self.containerVideoCamera.grid_columnconfigure(0, weight=1)

        self.makePhanDoanBaoLucGUI6()

        # self.videoLoadingThreading()
        self.root.wm_protocol('VM_DELETE_WINDOW', self.onClose)
        self.fun_initGUI()
        self.fun_taiGiaoDien17CapDo()

    def fun_initGUI(self):
        img = cv2.imread(filename= 'FileInput/Imgs/ImgNotFound2.jpg')
        img1 = cv2.imread(filename= 'FileInput/Imgs/ImgNotFound.jpg')
        size = libs.fun_getSizeOfFrame(frame= img)
        size1 = libs.fun_getSizeOfFrame(frame= img1)
        self.imgNotFound = libs.fun_cv2_imageArrayToImage(containerFather= self.containerVideoCamera, frame= img, reSize= size)
        self.imgNotFound1 = libs.fun_cv2_imageArrayToImage(containerFather= self.containerVideoCamera, frame= img1, reSize= (int(size[0] * 0.2), int(size[1] * 0.2)))
        self.lbVideoFrames.config(image= self.imgNotFound)
        self.lbVideoFrames1.config(image= self.imgNotFound1)
        self.lbVideoFrames2.config(image= self.imgNotFound1)
        self.lbVideoFrames3.config(image= self.imgNotFound1)
        self.lbVideoFrames4.config(image= self.imgNotFound1)

    def fun_ngatKetNoi(self):
        if self.stopEvent is None:
            return
        self.stopEvent.set()
        self.fun_initGUI()

    def fun_taiLaiVideo(self):
        self.btnRefresh.config(state='disable', cursor=CURSOR_NO)
        self.videoLoadingThreading()

    def fun_taiGiaoDien17CapDo(self):
        # Giao dien cho container 17 Cap do
        self.arrACTION.clear()
        actionNames = cf.VIDEO_NAMES.copy()
        actionNames.insert(0, 'no')
        for i in range(0, len(actionNames)):
            action = Label(self.containerChucNang, bg='#ffffff', padx=10, pady=10,
                           text=actionNames[i],
                           font=('Helvetica', 18, 'bold')
                           )
            action.grid(row=0, column=i, sticky='nsew')
            self.arrACTION.append(action)

        self.containerChucNang.grid_rowconfigure(0, weight=1)
        for i in range(0, len(actionNames)):
            self.containerChucNang.grid_columnconfigure(i, weight=1)

    # event cho button chon nguon du lieu
    def fun_chonNguonDuLieu(self):
        self.newWindow = Toplevel(self.root)
        self.app = ChoseSourceWindow(self.newWindow)
        self.app.master.grab_set()
        self.root.wait_window(self.app.master)
        # Hanh dong khong duoc xac thuc tu nguoi dung -> ket thuc
        if not self.app.DIALOG_OK:
            messagebox.showwarning('Thong Bao', 'Chon nguon video that bai')
            return
        # Hang dong duoc xac thuc tu phai nguoi dung
        self.URL_VIDEO = self.app.RETURN_RESULT
        self.fun_taiGiaoDien17CapDo()
        self.videoLoadingThreading()

    def makePhanDoanBaoLucGUI6(self):
        self.frameVideo1 = Frame(self.containerPhanDoanBaoLuc, padx=10, pady=10, bg='white')
        self.frameVideo2 = Frame(self.containerPhanDoanBaoLuc, padx=10, pady=10, bg='#c1ffe5')
        self.frameVideo3 = Frame(self.containerPhanDoanBaoLuc, padx=10, pady=10, bg='#c1ffe5')
        self.frameVideo4 = Frame(self.containerPhanDoanBaoLuc, padx=10, pady=10, bg='white')

        self.frameVideo1.grid(row=0, column=0, sticky='nsew')
        self.frameVideo2.grid(row=0, column=1, sticky='nsew')
        self.frameVideo3.grid(row=1, column=0, sticky='nsew')
        self.frameVideo4.grid(row=1, column=1, sticky='nsew')

        self.containerPhanDoanBaoLuc.grid_rowconfigure(0, weight=1)
        self.containerPhanDoanBaoLuc.grid_rowconfigure(1, weight=1)
        self.containerPhanDoanBaoLuc.grid_columnconfigure(0, weight=1)
        self.containerPhanDoanBaoLuc.grid_columnconfigure(1, weight=1)

        # phan doan 1
        self.lbVideoFrames1 = Label(self.frameVideo1, padx=10, pady=10, bg='white')
        self.lbVideoFrames1.grid(row=0, column=0, sticky='nsew')
        self.frameVideo1.grid_rowconfigure(0, weight=1)
        self.frameVideo1.grid_columnconfigure(0, weight=1)

        # phan doan 2
        self.lbVideoFrames2 = Label(self.frameVideo2, padx=10, pady=10, bg='white')
        self.lbVideoFrames2.grid(row=0, column=0, sticky='nsew')
        self.frameVideo2.grid_rowconfigure(0, weight=1)
        self.frameVideo2.grid_columnconfigure(0, weight=1)

        # phan doan 3
        self.lbVideoFrames3 = Label(self.frameVideo3, padx=10, pady=10, bg='white')
        self.lbVideoFrames3.grid(row=0, column=0, sticky='nsew')
        self.frameVideo3.grid_rowconfigure(0, weight=1)
        self.frameVideo3.grid_columnconfigure(0, weight=1)

        # phan doan 4
        self.lbVideoFrames4 = Label(self.frameVideo4, padx=10, pady=10, bg='white')
        self.lbVideoFrames4.grid(row=0, column=0, sticky='nsew')
        self.frameVideo4.grid_rowconfigure(0, weight=1)
        self.frameVideo4.grid_columnconfigure(0, weight=1)

        self.arrThread = []
        thread1 = MyThreadingVideo(lbShow=self.lbVideoFrames1, lbFather=self.frameVideo1, lbShowKetQua= self.lbKetQuaBaoLuc, vgg16_model= self.vgg16_model, lstm_model= self.lstm_model, treeAction=None)
        thread2 = MyThreadingVideo(lbShow=self.lbVideoFrames2, lbFather=self.frameVideo2, lbShowKetQua= self.lbKetQuaBaoLuc, vgg16_model= self.vgg16_model, lstm_model= self.lstm_model, treeAction=None)
        thread3 = MyThreadingVideo(lbShow=self.lbVideoFrames3, lbFather=self.frameVideo3, lbShowKetQua= self.lbKetQuaBaoLuc, vgg16_model= self.vgg16_model, lstm_model= self.lstm_model, treeAction=None)
        thread4 = MyThreadingVideo(lbShow=self.lbVideoFrames4, lbFather=self.frameVideo4, lbShowKetQua= self.lbKetQuaBaoLuc, vgg16_model= self.vgg16_model, lstm_model= self.lstm_model, treeAction=None)
        self.arrThread.append(thread1)
        self.arrThread.append(thread3)
        self.arrThread.append(thread4)
        self.arrThread.append(thread2)

    def runMyApp(self):
        self.root.mainloop()

    def videoLoadingThreading(self):
        self.stopEvent = threading.Event()
        self.loadVideoThread = threading.Thread(target=self.updateVideoFrames, args=())
        self.loadVideoThread.setDaemon(True)
        self.loadVideoThread.start()

    def updateVideoFrames(self):
        self.videoCap = cv2.VideoCapture(self.URL_VIDEO)
        self.isContinue, self.frame = self.videoCap.read()

        count = 0
        xoayVong = 0
        frames = []
        while not self.stopEvent.is_set() and self.isContinue:
            image = libs.fun_cv2_imageArrayToImage(containerFather= self.containerVideoCamera, frame= self.frame.copy())

            self.lbVideoFrames.config(image=image)
            self.lbVideoFrames.image = image
            isContinue, self.frame = self.videoCap.read()
            # Doc khong duoc la het video -> nho thoat ra
            if not isContinue:
                break

            frames.append(self.frame.copy())
            cv2.waitKey(5)
            if count == 19:
                self.arrThread[xoayVong].setFrames(frames)
                self.arrThread[xoayVong].startShowVideo()
                xoayVong += 1
                if xoayVong == 4:
                    xoayVong = 0

                frames = []
                count = 0
                continue
            count += 1

        self.btnRefresh.config(state='normal', cursor=CURSOR_DF)
        if not self.IS_PAUSE:
            self.videoCap.release()

    def onClose(self):
        libs.fun_print(name='Violence Detect App', value='Closing')
        self.videoCap.release()
        self.root.destroy()
        sys.exit(0)

if __name__ == '__main__':
    if IS_USING_WEBCAM:
        URL_VIDEO = 0
    videoCap = cv2.VideoCapture(URL_VIDEO)
    app = MyApp()
    app.runMyApp()
