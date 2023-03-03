from tkinter import *
from tkinter import messagebox, filedialog
# from Modules.LSTM_Config import *
from Modules import PublicModules as libs
from Modules import LSTM_Config as cf
import cv2
import threading
from PIL import Image
from PIL import ImageTk
from Modules.MyComponents import *
from tkcalendar import Calendar
from Modules.MyThreading import MyThreadingVideo # lấy cái của em nó không nahanj được mấy cái url này ne nên lỗi khác
from threading import Thread
from time import sleep
from datetime import datetime
# from HanhViBaoLuc import RealTime_URL as RT
import RealTime_URL as RT
# from HanhViBaoLuc.Modules.MyComponents import TreeActionDetection
from Modules.MyComponents import TreeActionDetection

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
        self.master = master
        self.master.minsize(500, 100)
        self.frame = Frame(self.master)
        # self.master.grab_set()
        libs.fun_makeCenter(self.master)
        self.DIALOG_OK = False
        self.RETURN_RESULT = 'NULL'
        self.iconCheck = PhotoImage(file='FileInput/Icons/ic_check2.png').subsample(3, 3)
        self.iconMp4 = PhotoImage(file='FileInput/Icons/ic_mp4.png').subsample(3, 3)
        self.iconCamera = PhotoImage(file='FileInput/Icons/ic_mp4.png').subsample(3, 3)

        # goi sau cung nhe
        self.fun_initComponent()

    # Ham chon URL
    def fun_initComponent(self):
        self.frame.grid(row=0, column=0, sticky='nsew')
        self.master.grid_columnconfigure(0, weight=1)
        self.master.grid_rowconfigure(0, weight=1)

        # frame 1
        # màu khung của Frame taio l
        self.frame1 = Frame(self.frame, bg='#4c5254', padx=10, pady=10)
        self.frame2 = Frame(self.frame, bg='#81968e', padx=10, pady=10)
        self.frame3 = Frame(self.frame, bg='#4c5254', padx=10, pady=10)

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
                                    font=('Helvetica', 18, 'bold',),
                                    cursor=CURSOR_DF
                                    )

        self.checkDir.grid(row=0, column=0, sticky='nsew')
        self.frame1.grid_rowconfigure(0, weight=1)
        self.frame1.grid_columnconfigure(0, weight=1)

        self.tbSource = EntryWithPlaceholder(self.frame2, 'IP WEBCAM EXAMPLE: 192.168.1.1')
        self.tbSource.grid(row=0, column=0, sticky='nsew')

        # BTN Check
        self.btnSource = Button(self.frame2,
                                command=self.btnGetPathFromSourceClicked, cursor=CURSOR_DF,
                                image=self.iconCheck,
                                compound=CENTER,
                                bg='#81968e'
                                )
        self.btnSource.grid(row=0, column=1, sticky='nsew')

        self.frame2.grid_columnconfigure(0, weight=9)
        self.frame2.grid_columnconfigure(1, weight=1)
        self.frame2.grid_rowconfigure(0, weight=1)
        # BTN LOAD
        self.btnOk = Button(self.frame3, padx=10, pady=10, text='Load Video Clip Detection'
                            , command=self.btnLoadVideoClicked,
                            state='disable',
                            cursor=CURSOR_NO
                            )
        self.btnOk.grid(row=0, column=0, sticky='nsew')
        self.frame3.grid_columnconfigure(0, weight=1)
        self.frame3.grid_rowconfigure(0, weight=1)

        # ----------- bo di tinh nang camera -------------
        # self.isUsingIpWebcam.set(1)
        # self.fun_CheckIsUsingCamChange()
        # self.checkDir.config(state='disable')
        # ------------------------------------------------

    def fun_CheckIsUsingCamChange(self):
        if self.isUsingIpWebcam.get() == 0:
            self.btnSource.config(image=self.iconCheck)
            holder = 'IP WEBCAM EXAMPLE: 192.168.1.1'
            self.checkDir.config(bg='white')
        else:
            self.btnSource.config(image=self.iconMp4)
            holder = 'EXAMPLE: C:/VIDEO/DETECTION.MP4'
            self.checkDir.config(bg='#81968e')

        self.fun_reloadHolderSource(source=holder)

    def fun_reloadHolderSource(self, source: str):
        self.tbSource.delete('0', 'end')
        self.tbSource.placeholder = source
        self.tbSource.put_placeholder()

    def fun_checkVideoFromSource(self, source: str):
        try:
            # frames = libs.fun_getFramesOfVideo(path=source, count=24)
            messagebox.showinfo('Notification!', 'Check Video Load OK, Video Size: {0}')
            return True
        except:
            messagebox.showerror('Error!', 'Request is not accepted')
            return False

    def fun_getURL_IPCam(self, ip: str):
        return '{0}{1}{2}'.format('http://', ip, ':4747/video')

    def btnLoadVideoClicked(self):
        if self.isUsingIpWebcam.get() == 0:
            self.RETURN_RESULT = self.fun_getURL_IPCam(ip=self.tbSource.get())
        self.DIALOG_OK = True
        self.master.destroy()

    def btnGetPathFromSourceClicked(self):
        if self.isUsingIpWebcam.get() == 0:
            url = self.fun_getURL_IPCam(ip=self.tbSource.get())
            # self.RETURN_RESULT = 'FileInput/Test/LtestFull.avi'
        else:
            self.RETURN_RESULT = filedialog.askopenfilename(initialdir="FileInput/Test/", title="Select file",
                                                            filetypes=(("AVI files", "*.AVI"), ("MP4 files", "*.MP4"),
                                                                       ("ALL files", "*.*")))
            self.fun_reloadHolderSource(source=self.RETURN_RESULT)
            url = self.RETURN_RESULT
        isCheck = self.fun_checkVideoFromSource(source=url)
        if isCheck:
            self.btnOk.config(state='normal', cursor=CURSOR_DF)
        else:
            self.btnOk.config(state='disable', cursor=CURSOR_NO)

    def close_windows(self):
        self.master.destroy()


class ChoseSourceWindow_Camera:
    def __init__(self, master):
        self.isUsingIpWebcam = IntVar()
        self.master = master
        self.master.minsize(500, 100)
        self.frame = Frame(self.master)
        # self.master.grab_set()
        libs.fun_makeCenter(self.master)
        self.DIALOG_OK = False
        self.RETURN_RESULT = 'NULL'
        self.iconCheck = PhotoImage(file='FileInput/Icons/ic_check2.png').subsample(3, 3)
        self.iconMp4 = PhotoImage(file='FileInput/Icons/ic_mp4.png').subsample(3, 3)
        self.iconCamera = PhotoImage(file='FileInput/Icons/ic_mp4.png').subsample(3, 3)

        # goi sau cung nhe
        self.fun_initComponent()

    # Ham chon URL
    def fun_initComponent(self):
        self.frame.grid(row=0, column=0, sticky='nsew')
        self.master.grid_columnconfigure(0, weight=1)
        self.master.grid_rowconfigure(0, weight=1)

        # frame 1
        self.frame1 = Frame(self.frame, bg='#4c5254', padx=10, pady=10)
        self.frame2 = Frame(self.frame, bg='#81968e', padx=10, pady=10)
        self.frame3 = Frame(self.frame, bg='#4c5254', padx=10, pady=10)

        self.frame1.grid(row=0, column=0, sticky='nsew')
        self.frame2.grid(row=1, column=0, sticky='nsew')
        self.frame3.grid(row=2, column=0, sticky='nsew')

        self.frame.grid_columnconfigure(0, weight=1)
        self.frame.grid_rowconfigure(0, weight=1)
        self.frame.grid_rowconfigure(1, weight=1)
        self.frame.grid_rowconfigure(2, weight=1)

        self.frame1.grid_rowconfigure(0, weight=1)
        self.frame1.grid_columnconfigure(0, weight=1)

        self.tbSource = EntryWithPlaceholder(self.frame2, 'IN TO WEBCAM')
        self.tbSource.grid(row=0, column=0, sticky='nsew')

        # BTN Check
        self.btnSource = Button(self.frame2,
                                command=self.btnGetPathFromSourceClicked, cursor=CURSOR_DF,
                                image=self.iconCheck,
                                compound=CENTER,
                                bg='#81968e'
                                )
        self.btnSource.grid(row=0, column=1, sticky='nsew')

        self.frame2.grid_columnconfigure(0, weight=9)
        self.frame2.grid_columnconfigure(1, weight=1)
        self.frame2.grid_rowconfigure(0, weight=1)
        # BTN LOAD
        self.btnOk = Button(self.frame3, padx=10, pady=10, text='Load Video Clip Detection'
                            , command=self.btnLoadVideoClicked,
                            state='disable',
                            cursor=CURSOR_NO
                            )
        self.btnOk.grid(row=0, column=0, sticky='nsew')
        self.frame3.grid_columnconfigure(0, weight=1)
        self.frame3.grid_rowconfigure(0, weight=1)

    def fun_reloadHolderSource(self, source: str):
        self.tbSource.delete('0', 'end')
        self.tbSource.placeholder = source
        self.tbSource.put_placeholder()

    def fun_checkVideoFromSource(self, source: str):
        try:
            messagebox.showinfo('Notification!', 'Check Camera Load OK')
            return True
        except:
            messagebox.showerror('Error!', 'Request is not accepted')
            return False

    def fun_getURL_IPCam(self, ip: str):
        return '{0}{1}{2}'.format('http://', ip, ':4747/video')

    def btnLoadVideoClicked(self):
        if self.isUsingIpWebcam.get() == 0:
            self.RETURN_RESULT = self.fun_getURL_IPCam(ip=self.tbSource.get())
        self.DIALOG_OK = True
        self.master.destroy()

    def btnGetPathFromSourceClicked(self):
        self.RETURN_RESULT = 'FileInput/Imgs/L22_0001.avi'
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
        self.stopEvent = None
        self.IS_PAUSE = False
        self.isChoiseTimeDown = IntVar()
        self.valHourDown = StringVar()
        self.valMinuteDown = StringVar()

        self.containerTrai = None
        self.containerPhai = None

        self.root.minsize(width=WINDOWS_WIDTH, height=WINDOWS_HEIGHT)
        # libs.fun_makeCenter(self.root)
        libs.fun_makeMaximumSize(self.root)

        # Load model VGG16
        self.vgg16_model = cf.fun_getVGG16Model()
        self.vgg16_model.summary()

        # Load model LSTM
        try:
            self.lstm_model = cf.fun_loadModelLSTM()
            self.lstm_model.summary()
        except:
            print('# ' * 20)
            libs.fun_print(name='Load Model LSTM', value='File Not Found!');
            return

        self.initComponent()

    def fun_updateTimeDown(self, ):
        while self.isChoiseTimeDown.get():
            _, current = libs.fun_getCurrentTime()
            isContinue, self.down = libs.fun_dayMinus(dayFrom=current, dayTo=self.dayTo)
            try:
                if not isContinue:
                    self.isChoiseTimeDown.set(0)
                    self.cbThoiGianTat.config(bg='white')
                    self.lbThoiGianDen.destroy()
                    self.lbThoiGianConLai.destroy()
                    self.fun_ngatKetNoi()
                    self.fun_ngatKetNoi()
                    return
                self.lbThoiGianConLai.config(text='Time remaining: {0} s'.format(self.down))
            except:
                print('Thread update time down stoped but not working correct!' + Thread.name)
            sleep(1)

    def fun_hienThoiGianDen_Con(self, timeDown: str, hourDown: int, minuteDown: int):
        self.lbThoiGianConLai = Label(self.containerTongHopMoTaPhanDoanDanh,
                                      text='Time remaining: {0} s'.format(self.down),
                                      padx=10, pady=10,
                                      font=('Helvetica', 18, 'bold'),
                                      anchor='w'
                                      )

        self.lbThoiGianDen = Label(self.containerTongHopMoTaPhanDoanDanh,
                                   text='Off on time: {0} {1}h : {2}m'.format(timeDown, hourDown, minuteDown),
                                   padx=10, pady=10,
                                   font=('Helvetica', 18, 'bold'),
                                   anchor='w'
                                   )

        self.lbThoiGianDen.grid(row=1, column=0, sticky='nsew')
        self.lbThoiGianConLai.grid(row=2, column=0, sticky='nsew')

        self.containerTongHopMoTaPhanDoanDanh.grid_rowconfigure(1, weight=1)
        self.containerTongHopMoTaPhanDoanDanh.grid_rowconfigure(2, weight=1)

    def fun_chonThoiGianDen(self, ):
        isChoise = self.isChoiseTimeDown.get()
        self.TIME_DOWN = None
        # 1 => get | 0 - not

        if not isChoise:
            self.lbThoiGianDen.destroy()
            self.lbThoiGianConLai.destroy()
            self.cbThoiGianTat.config(bg='white')
            return

        self.cbThoiGianTat.config(bg='#4c5254')
        # hien thi form chon thoi gian
        top = Toplevel(self.root)
        cal = Calendar(top,
                       font="Arial 14", selectmode='day',
                       cursor="hand1", )
        cal.selection_set(datetime.now())
        cal.pack(fill="both", expand=True)
        self.tbHour = EntryWithPlaceholder(top, placeholder='HOURS SELECTED')
        self.tbHour.pack(fill='both', expand=True, padx=10, pady=10)
        self.tbMinute = EntryWithPlaceholder(top, placeholder='MINUTE SELECTED')
        self.tbMinute.pack(fill='both', expand=True, padx=10, pady=10)
        Button(top, text="APPLY NOW", command=self.fun_btnOkChoiDataTimeDownClicked).pack(fill='both', expand=True,
                                                                                          padx=10, pady=10)
        self.cal = cal
        self.ChoiseTimeDownWindow = top
        top.grab_set()
        self.root.wait_window(top)

        if self.TIME_DOWN is None:
            messagebox.showwarning('Notification!', 'Time to automatically turn off failure')
            self.isChoiseTimeDown.set(0)
            self.cbThoiGianTat.config(bg='white')
            return

        # khiem tra thoi gian hop le tai day
        _, curent = libs.fun_getCurrentTime()
        ymd = str(self.TIME_DOWN).split('-')
        dayTo = '{0}_{1}_{2}_{3}_{4}_{5}'.format(ymd[0], ymd[1], ymd[2], self.hourDown, self.minuteDown, 0)
        isCheck, down = libs.fun_dayMinus(dayFrom=curent, dayTo=dayTo)

        if not isCheck:
            messagebox.showerror('Notification!', 'Time to automatically turn off failure')
            self.isChoiseTimeDown.set(0)
            self.cbThoiGianTat.config(bg='white')
            return

        self.dayTo = dayTo
        self.down = down

        self.fun_hienThoiGianDen_Con(timeDown=self.TIME_DOWN, hourDown=self.hourDown, minuteDown=self.minuteDown)
        # start new thread update downtime
        self.threadUpdateTime = Thread(target=self.fun_updateTimeDown)
        self.threadUpdateTime.setDaemon(True)
        self.threadUpdateTime.start()

    def fun_btnOkChoiDataTimeDownClicked(self, ):
        self.TIME_DOWN = self.cal.selection_get()
        hour = 0
        minu = 0
        try:
            hour = int(self.tbHour.get())
            minu = int(self.tbMinute.get())
        except:
            messagebox.showwarning('Error!',
                                   'Error white pare hour and minute-> but default [ {0} : {1} ]'.format(hour, minu))

        self.hourDown = hour
        self.minuteDown = minu
        self.ChoiseTimeDownWindow.destroy()

    def initComponent(self):
        #
        self.containerTrai = Frame(self.root, bg='white', padx=10, pady=10)
        self.containerPhai = Frame(self.root, bg='white', padx=10, pady=10)

        self.containerTrai.grid(row=0, column=0, sticky='nsew')
        self.containerPhai.grid(row=0, column=1, sticky='nsew')

        self.root.grid_columnconfigure(0, weight=8)
        self.root.grid_columnconfigure(1, weight=2)
        self.root.grid_rowconfigure(0, weight=1)

        # Container con cua trai
        # Khung tai lên và khung Cây
        self.containerChonNguonDuLieu = Frame(self.containerTrai, bg='#4c5254', padx=10, pady=10)
        # Màu hiện thị khung show
        self.containerVideoCamera = Frame(self.containerTrai, bg='#81968e', padx=10, pady=10)
        # màu hiện thị label bạo lực
        self.containerChucNang = Frame(self.containerTrai, bg='#99f2c1', padx=10, pady=10)

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
        self.btnChonNguonDuLieu.image = iconChonNguonDuLieu
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
        self.btnRefresh.image = iconTaiLaiVideo
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

        self.btnDisconection.imgage = iconNgatKetNoi
        self.btnDisconection.grid(row=0, column=2, sticky='nsew')

        # Giao dien cho nut mo camera May tinh
        iconCamera = PhotoImage(file='FileInput/Icons/ic_camera.png')
        iconCamera = iconCamera.subsample(1, 1)
        self.btnCamera = Button(self.containerChonNguonDuLieu, padx=10,
                                pady=10,
                                image=iconCamera,
                                command=self.fun_chonNguonDuLieu_camera,
                                cursor=CURSOR_DF,
                                compound=RIGHT
                                )
        self.btnCamera.imgage = iconCamera
        self.btnCamera.grid(row=0, column=3, sticky='nsew')

        self.containerChonNguonDuLieu.grid_columnconfigure(0, weight=8)
        self.containerChonNguonDuLieu.grid_columnconfigure(1, weight=1)
        self.containerChonNguonDuLieu.grid_columnconfigure(2, weight=1)
        self.containerChonNguonDuLieu.grid_rowconfigure(0, weight=1)

        # Container con cua phai
        # Màu khung của My ap
        self.containerPhanDoanBaoLuc = Frame(self.containerPhai, bg='#99f2c1', padx=10, pady=10)

        self.containerTongHopMoTaPhanDoanDanh = Frame(self.containerPhai, bg='#81968e', padx=10, pady=10)

        self.containerPhanDoanBaoLuc.grid(row=0, column=0, sticky='nsew')
        self.containerTongHopMoTaPhanDoanDanh.grid(row=1, column=0, sticky='nsew')

        # Container con cua container containerTongHopMoTaPhanDoanDanh
        self.cbThoiGianTat = Checkbutton(self.containerTongHopMoTaPhanDoanDanh,
                                         text='TIME SELECTION AUTO OFF', padx=10, pady=10,
                                         font=('Helvetica', 18, 'bold'),
                                         variable=self.isChoiseTimeDown,
                                         command=self.fun_chonThoiGianDen,
                                         )
        self.cbThoiGianTat.grid(row=0, column=0, sticky='nsew')

        self.containerTongHopMoTaPhanDoanDanh.grid_rowconfigure(0, weight=1)
        self.containerTongHopMoTaPhanDoanDanh.grid_columnconfigure(0, weight=1)

        # Label hien thi loai bao luc gi
        self.lbKetQuaBaoLuc = Label(self.containerChucNang,
                                    text='TÊN BẠO LỰC HIỂN THỊ TẠI ĐÂY', padx=10,
                                    pady=10,
                                    bg='white',
                                    font=('Helvetica', 18, 'bold')
                                    )
        self.lbKetQuaBaoLuc.grid(row=0, column=0, sticky='nsew')
        self.containerChucNang.grid_rowconfigure(0, weight=1)
        self.containerChucNang.grid_columnconfigure(0, weight=1)

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
        img = cv2.imread(filename='FileInput/Imgs/ImgNotFound2.jpg')
        img1 = cv2.imread(filename='FileInput/Imgs/ImgNotFound.jpg')
        size = libs.fun_getSizeOfFrame(frame=img)
        self.imgNotFound = libs.fun_cv2_imageArrayToImage(containerFather=self.containerVideoCamera, frame=img,
                                                          reSize=size)
        self.imgNotFound1 = libs.fun_cv2_imageArrayToImage(containerFather=self.containerVideoCamera, frame=img1,
                                                           reSize=(int(size[0] * 0.2), int(size[1] * 0.2)))
        self.lbVideoFrames.config(image=self.imgNotFound)
        self.lbVideoFrames1.config(image=self.imgNotFound1)
        self.lbVideoFrames2.config(image=self.imgNotFound1)
        self.lbVideoFrames3.config(image=self.imgNotFound1)
        self.lbVideoFrames4.config(image=self.imgNotFound1)

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
        pass  # Nothing to do

    # event cho button chon nguon du lieu
    def fun_chonNguonDuLieu(self):
        self.newWindow = Toplevel(self.root)
        self.app = ChoseSourceWindow(self.newWindow)
        self.app.master.grab_set()
        self.root.wait_window(self.app.master)
        # Hanh dong khong duoc xac thuc tu nguoi dung -> ket thuc
        if not self.app.DIALOG_OK:
            messagebox.showwarning('Notification!', 'Failed video source selection')
            return
        # Hang dong duoc xac thuc tu phai nguoi dung
        self.URL_VIDEO = self.app.RETURN_RESULT
        self.fun_taiGiaoDien17CapDo()
        # Truoc khi load video va xem ket qua, kich hoat GPU len truoc
        self.fun_kichHoatGPU_Threading()

        # Thuc hien load video bang threading va xem ket qua
        # self.videoLoadingThreading()

    def fun_chonNguonDuLieu_camera(self):
        # Hanh dong khong duoc xac thuc tu nguoi dung -> ket thuc
        self.newWindow = Toplevel(self.root)
        self.app = ChoseSourceWindow_Camera(self.newWindow)
        self.app.master.grab_set()
        self.root.wait_window(self.app.master)
        if not self.app.DIALOG_OK:
            messagebox.showwarning('Notification!', 'Failed video source selection')
            return
        # Hang dong duoc xac thuc tu phai nguoi dung

        self.URL_VIDEO = self.app.RETURN_RESULT
        ##----------- Xát nhận mở camera
        self.URL_VIDEO = 0
        # self.URL_VIDEO = 'http://192.168.5.254:2109/video'
        self.fun_taiGiaoDien17CapDo()
        # Truoc khi load video va xem ket qua, kich hoat GPU len truoc
        self.fun_kichHoatGPU_Threading()

        # Thuc hien load video bang threading va xem ket qua
        # self.videoLoadingThreading()

    def fun_kichHoatGPU(self, ):
        # khoi tao kich hoat
        self.ACTIVE_OK = False

        # show by threading
        t_loading = threading.Thread(target=self.fun_showLoadingCMD, args=())
        t_loading.setDaemon(True)
        t_loading.start()

        # kich hoat GPU
        MAX_COUNT_ACTIVE = 5
        for _ in range(MAX_COUNT_ACTIVE):
            _25F = libs.fun_getFramesOfVideo(self.URL_VIDEO, count= 10)
            # _20F = libs.fun_getFramesOfVideo(self.URL_VIDEO, count= 20)
            transfer = cf.fun_getTransferValue_EDIT(pathVideoOrListFrame=_25F, modelVGG16=self.vgg16_model);
            libs.fun_predict(modelLSTM=self.lstm_model, transferValue=transfer, isPrint=True)

        # OK
        self.ACTIVE_OK = True

    def fun_showLoadingCMD(self, ):
        while not self.ACTIVE_OK:
            os.system("start /wait cmd /c LoadingCMD.py")
        self.videoLoadingThreading()

    def fun_kichHoatGPU_Threading(self, ):
        t_kichHoatGPU = threading.Thread(target=self.fun_kichHoatGPU, args=())
        t_kichHoatGPU.setDaemon(True)
        t_kichHoatGPU.start()

    def makePhanDoanBaoLucGUI6(self):
        self.treeAction = TreeActionDetection(containerFather=self.containerPhanDoanBaoLuc)

        self.frameVideo1 = Frame(self.containerPhanDoanBaoLuc, padx=10, pady=10, bg='white')
        self.frameVideo2 = Frame(self.containerPhanDoanBaoLuc, padx=10, pady=10, bg='#81968e')
        self.frameVideo3 = Frame(self.containerPhanDoanBaoLuc, padx=10, pady=10, bg='#c1ffe5')
        self.frameVideo4 = Frame(self.containerPhanDoanBaoLuc, padx=10, pady=10, bg='white')

        '''
            Lam chi de cho LeanGUI thoi LeanGUI_V2 Khong co su dung ne tat di
            So di lam nhu vay boi bi khong phai dung cham gi den cac class khac
            ma van hoat dong duoc tren ca 2 GUI va GUI V2
        '''
        # self.frameVideo1.grid(row=0, column=0, sticky='nsew')
        # self.frameVideo2.grid(row=0, column=1, sticky='nsew')
        # self.frameVideo3.grid(row=1, column=0, sticky='nsew')
        # self.frameVideo4.grid(row=1, column=1, sticky='nsew')

        self.containerPhanDoanBaoLuc.grid_rowconfigure(0, weight=1)
        # self.containerPhanDoanBaoLuc.grid_rowconfigure(1, weight=1)
        self.containerPhanDoanBaoLuc.grid_columnconfigure(0, weight=1)
        # self.containerPhanDoanBaoLuc.grid_columnconfigure(1, weight=1)

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
        thread1 = MyThreadingVideo(lbShow=None, lbFather=self.frameVideo1, lbShowKetQua=self.lbKetQuaBaoLuc,
                                   vgg16_model=self.vgg16_model, lstm_model=self.lstm_model, treeAction=self.treeAction)
        thread2 = MyThreadingVideo(lbShow=None, lbFather=self.frameVideo2, lbShowKetQua=self.lbKetQuaBaoLuc,
                                   vgg16_model=self.vgg16_model, lstm_model=self.lstm_model, treeAction=self.treeAction)
        thread3 = MyThreadingVideo(lbShow=None, lbFather=self.frameVideo3, lbShowKetQua=self.lbKetQuaBaoLuc,
                                   vgg16_model=self.vgg16_model, lstm_model=self.lstm_model, treeAction=self.treeAction)
        thread4 = MyThreadingVideo(lbShow=None, lbFather=self.frameVideo4, lbShowKetQua=self.lbKetQuaBaoLuc,
                                   vgg16_model=self.vgg16_model, lstm_model=self.lstm_model, treeAction=self.treeAction)
        self.arrThread.append(thread1)
        self.arrThread.append(thread3)
        self.arrThread.append(thread4)
        self.arrThread.append(thread2)

    def runMyApp(self):
        self.root.mainloop()

    def videoLoadingThreading(self):
        self.stopEvent = threading.Event()
        # gọi thằng mythreading
        self.loadVideoThread = threading.Thread(target=self.updateVideoFrames, args=()) # load vào luồng... r lấy luồng gọi cuối cùng đưa qua mảng
        self.loadVideoThread.setDaemon(True)
        self.loadVideoThread.start()

    def updateVideoFrames(self):
        self.videoCap = cv2.VideoCapture(self.URL_VIDEO)
        self.isContinue, self.frame = self.videoCap.read()

        count = 0
        xoayVong = 0
        frames = []
        while not self.stopEvent.is_set() and self.isContinue:
            image = libs.fun_cv2_imageArrayToImage(containerFather=self.containerVideoCamera, frame=self.frame.copy())
            self.lbVideoFrames.config(image=image)
            self.lbVideoFrames.image = image
            isContinue, self.frame = self.videoCap.read()
            # Doc khong duoc la het video -> nho thoat ra
            if not isContinue:
                break
            frames.append(self.frame.copy())
            cv2.waitKey(7)
            # cf.NUM_FRAME_INPUT_LSTM - 1
            # Lay 24 bo vao mang de viet ham lấy ra 10 Frame
            if count == 24:
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
    # if IS_USING_WEBCAM:
    #     URL_VIDEO = 0
    # videoCap = cv2.VideoCapture()
    app = MyApp()
    app.runMyApp()
