import os
import cv2
import sys
import zipfile
from PIL import Image
from PIL import ImageTk
import numpy as np
from datetime import datetime, timedelta

def fun_print(name: str, value) -> None:
    print('@ Deep Learning> ', name)
    print(value)


def fun_getFileNames(path: str) -> list:
    return os.listdir(path)


def fun_showVideoPath(path: str, delay: int = 25, title= 'frame') -> None:
    cap = cv2.VideoCapture(path)
    isContinue, frame = cap.read()
    while True:
        if not isContinue:
            break
        cv2.imshow(title, frame)
        if cv2.waitKey(delay=delay) & 0xFF == ord('q'):
            break
        isContinue, frame = cap.read()

    cap.release()
    cv2.destroyAllWindows()


def fun_getFramesOfVideo(path: str, count: int = 30) -> list:
    cap = cv2.VideoCapture(path)
    isContinue, frame = cap.read()
    imgs = []
    while count > 0:
        if not isContinue:
            break
        imgs.append(frame)
        isContinue, frame = cap.read()
        count -= 1
    cap.release()
    cv2.destroyAllWindows()
    return imgs

def fun_getFramesOfVideo_ALL(path: str) -> list:
    cap = cv2.VideoCapture(path)
    isContinue, frame = cap.read()
    imgs = []
    while True:
        if not isContinue:
            break
        imgs.append(frame)
        isContinue, frame = cap.read()
    cap.release()
    cv2.destroyAllWindows()
    return imgs


def fun_showVideoFrames(frames: list, delay: int = 30, title= 'frame') -> None:
    for frame in frames:
        cv2.imshow(title, frame)
        if cv2.waitKey(delay=delay) & 0xFF == ord('q'):
            break


def fun_showVideo(source, delay: int = 30, title= 'frame') -> None:
    if isinstance(source, str):
        fun_showVideoPath(path=source, delay=delay, title= title)
    else:
        fun_showVideoFrames(frames=source, delay=delay, title= title)


def fun_resizeFrames(frames: list, size: tuple = (224, 224)) -> list:
    imgs = []
    count = 0
    for frame in frames:
        try:
          fr = cv2.resize(frame, dsize=size)
          imgs.append(fr)
        except:
          print('\r!Error To Resize of {0}'.format(count))
        count += 1
    cv2.destroyAllWindows()
    return imgs

# Danh nhan video
def fun_getVideoLabelNames_EachFolder(path: str):
    names = []

    for fol in os.listdir(path):
        folder = path + '/' + fol
        fileNames = fun_getFileNames(path=folder)
        for file in fileNames:
            names.append(file)

    return names

def fun_saveFramesToVideo(frames: list, path: str, fps: int = 30) -> bool:
    try:
        height, width, layer = frames[0].shape
        wr = cv2.VideoWriter(path, cv2.VideoWriter_fourcc(*'MJPG'), fps, (width, height))
        for frame in frames:
            wr.write(frame)
        wr.release()
        cv2.destroyAllWindows()
        return True

    except:
        fun_print(name='Write Video: '+path, value='ERROR TO WRITE VIDEO')
        cv2.destroyAllWindows()
        return False

def fun_getSizeOfFrame(frame) -> tuple:
    height, width, layer = frame.shape
    return (width, height)

# version 1
def fun_outListVideoWithNumFrame(dirInput: str, fileName: str, dirToSave: str, countFrame: int = 30, fps: int = 30, isShowCalculating: bool = False, isResize: bool= False):
    all = 0
    countWriten = 0

    pathVideo = dirInput + '/' + fileName
    if isShowCalculating:
        fun_print('Calculator Video Out Frame', 'calculating...')
        all = fun_getFramesOfVideo_ALL(pathVideo)
        all = len(all) // countFrame

    cap = cv2.VideoCapture(pathVideo)
    isContinue, frame = cap.read()
    count = 1
    while True:
        if not isContinue:
            break
        nameFile = dirToSave + '/' + fileName[0:len(fileName)-4] + '_[out_large_'+str(count)+'].avi'
        # nameFile = dirToSave + '/' + fileName[0:len(fileName)-4] + '_[out_large_'+str(count)+'].mp4'
        cFrame = countFrame
        frames = []

        # get list frame
        while cFrame > 0:
            frames.append(frame)
            isContinue, frame = cap.read()
            if not isContinue:
                break
            cFrame -= 1

        # check video enough frameCount frame ?
        if len(frames) != countFrame:
            break

        # write list frame
        if isResize:
            frames = fun_resizeFrames(frames= frames)
        res = fun_saveFramesToVideo(frames=frames, path=nameFile, fps=fps)
        countWriten += 1
        if res:
            if isShowCalculating:
                percent = countWriten / all
                mess = '\r - Writen: {0} -> Complete: {1:.1%}'.format(nameFile, percent)
                sys.stdout.write(mess)
                sys.stdout.flush()
            else:
                mess = '\r - Writen: {0} -> Complete'.format(nameFile)
                sys.stdout.write(mess)
                sys.stdout.flush()

        # done
        count += 1

    cap.release()
    cv2.destroyAllWindows()

def fun_extractZipFile(pathFileZip: str, pathToSave: str) -> None:
    if not os.path.exists(pathToSave):
        os.makedirs(pathToSave)
    fun_print(name='Extract All ' + pathFileZip, value='Extracting...')
    if (pathFileZip.endswith('.zip')):
        zipfile.ZipFile(file=pathFileZip, mode='r').extractall(path=pathToSave)
        print('Extract Done')
    else:
        print('Please Input zip file')

def fun_print_process(count: int, max: int, mess: str = 'Processing: ') -> None:
  process = count / max
  mess = '\r - ' +  mess + '{0:.2f}'.format(process * 100) + '% | ' + str(count) + '/' + str(max)
  sys.stdout.write(mess)
  sys.stdout.flush()

def fun_makeCenter(win):
    """
        centers a tkinter window
        :param win: the main window or Toplevel window to center
        """
    win.update_idletasks()
    width = win.winfo_width()
    frm_width = win.winfo_rootx() - win.winfo_x()
    win_width = width + 2 * frm_width
    height = win.winfo_height()
    titlebar_height = win.winfo_rooty() - win.winfo_y()
    win_height = height + titlebar_height + frm_width
    x = win.winfo_screenwidth() // 2 - win_width // 2
    y = win.winfo_screenheight() // 2 - win_height // 2
    win.geometry('{}x{}+{}+{}'.format(width, height, x, y))
    win.deiconify()

def fun_makeMaximumSize(root):
    # w, h = root.winfo_screenwidth(), root.winfo_screenheight()
    # root.geometry("%dx%d+0+0" % (w, h))
    root.state('zoomed')

def fun_cv2_imageArrayToImage(containerFather, frame, reSize=None):
    if reSize is None:
        winWidth = int(containerFather.winfo_width() * 0.9)
        winHeight = int(containerFather.winfo_height() * 0.9)
        frame = cv2.resize(frame, dsize=(winWidth, winHeight))
    elif isinstance(reSize, float):
        winWidth = int(containerFather.winfo_width() * reSize)
        winHeight = int(containerFather.winfo_height() * reSize)
        frame = cv2.resize(frame, dsize=(winWidth, winHeight))
    else:
        frame = cv2.resize(frame, dsize= reSize)
    image = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
    image = Image.fromarray(image)
    image = ImageTk.PhotoImage(image)
    return image

def fun_predict(modelLSTM, transferValue, isPrint: bool= False):
    arrPre = []
    arrPre.append(transferValue)
    Real = modelLSTM.predict(np.array(arrPre))
    pre = np.argmax(Real)

    if isPrint:
      print(Real, pre)
      print('\r')
    return pre, fun_MAX(Real[0])

def fun_MAX(arr):
    max = arr[0]
    count = 0
    for x in arr:
        if x > 0.09999999999 and x < 0.9999999999:
            count += 1
        if x > max:
            max = x
    if count == 3:
        return -1
    return max

def fun_getCurrentTime():
    time = datetime.now().isoformat()
    res = ''
    for c in time:
        if c == '-' or c == ':' or c == '.':
            res += '_'
        else:
            res += c
    
    tmps = res.split('T')
    left = tmps[0].split('_')
    right = tmps[1].split('_')

    # Y_M_D_h_m_s
    time = '{0}_{1}_{2}_{3}_{4}_{5}'.format(left[0], left[1], left[2], right[0], right[1], right[2])

    return res, time

def fun_dayMinus(dayFrom:str, dayTo:str):
    Y, M, D, h, m, s = 0, 1, 2, 3, 4, 5
    dayF = dayFrom.split('_')
    dayT = dayTo.split('_')

    dT = datetime(int(dayT[Y]), int(dayT[M]), int(dayT[D]), int(dayT[h]), int(dayT[m]), int(dayT[s]))
    dF = datetime(int(dayF[Y]), int(dayF[M]), int(dayF[D]), int(dayF[h]), int(dayF[m]), int(dayF[s]))
    
    res = str(dT - dF);
    return res[0] != '-', res

def fun_makeDir(directory: str):
    if not os.path.exists(directory):
        os.mkdir(directory)