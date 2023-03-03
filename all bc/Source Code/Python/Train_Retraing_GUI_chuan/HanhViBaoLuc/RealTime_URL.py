from models import *

import torch
from Modules import LSTM_Config as cf
from torchvision import datasets, transforms
from torch.autograd import Variable
from utils1 import utils
from Modules import PublicModules as libs
from PIL import Image
import cv2
import threading
from threading import Thread

# load weights and set defaults
config_path = 'config/yolov3-tiny.cfg'
weights_path = 'config/yolov3-tiny.weights'
class_path = 'config/yolov3.txt'
img_size = 416
conf_thres = 0.5
nms_thres = 0.4



# load model and put into eval mode
model = Darknet(config_path, img_size= img_size)
model.load_weights(weights_path)
model.cuda()
model.eval()

import os

os.environ['KMP_DUPLICATE_LIB_OK'] = 'True'

classes = None

with open(class_path, 'r') as f:
    classes = [line.strip() for line in f.readlines()]

COLORS = np.random.uniform(0, 255, size=(len(classes), 3))
net = cv2.dnn.readNet(weights_path, config_path)


def get_output_layers(net):
    layer_names = net.getLayerNames()

    output_layers = [layer_names[i[0] - 1] for i in net.getUnconnectedOutLayers()]

    return output_layers


# classes = utils.load_classes(class_path)


Tensor = torch.cuda.FloatTensor



def detect_image(img):
    # scale and pad image
    ratio = min(img_size / img.size[0], img_size / img.size[1])
    imw = round(img.size[0] * ratio)
    imh = round(img.size[1] * ratio)
    img_transforms = transforms.Compose([transforms.Resize((imh, imw)),
                                         transforms.Pad((max(int((imh - imw) / 2), 0), max(int((imw - imh) / 2), 0),
                                                         max(int((imh - imw) / 2), 0), max(int((imw - imh) / 2), 0)),
                                                        (128, 128, 128)),
                                         transforms.ToTensor(),
                                         ])
    # convert image to Tensor
    image_tensor = img_transforms(img).float()
    image_tensor = image_tensor.unsqueeze_(0)
    input_img = Variable(image_tensor.type(Tensor))

    # run inference on the model and get detections
    with torch.no_grad():
        detections = model(input_img)
        detections = utils.non_max_suppression(detections, 800, conf_thres, nms_thres)
    return detections[0]





# #
import cv2
from sort import *


# Lay Foldel
def fun_getFileNames(path: str) -> list:
    return os.listdir(path)

# Real Time Camera

def Remove_backgournd_RealTime(Array):
    _boxes =[]
    FRAME = []
    mot_tracker = Sort()
    frames = 0
    for frame in Array:
        frames += 1
        frame = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
        pilimg = Image.fromarray(frame)
        detections = detect_image(pilimg)
        frame = cv2.cvtColor(frame, cv2.COLOR_RGB2BGR)
        img = np.array(pilimg)
        pad_x = max(img.shape[0] - img.shape[1], 0) * (img_size / max(img.shape))
        pad_y = max(img.shape[1] - img.shape[0], 0) * (img_size / max(img.shape))
        unpad_h = img_size - pad_y
        unpad_w = img_size - pad_x
        boxes = []
        _frame = frame.copy()
        _frame *= 0
        if detections is not None:
            tracked_objects = mot_tracker.update(detections.cpu())
            unique_labels = detections[:, -1].cpu().unique()
            n_cls_preds = len(unique_labels)
            person = None
            _x1 = None
            _y1 = None
            _box_w = None
            _box_h = None
            dem = 0
            for x1, y1, x2, y2, obj_id, n_cls_preds in tracked_objects:
                if int(n_cls_preds) != 0:
                    continue
                box_h = int(((y2 - y1) / unpad_h) * img.shape[0])
                box_w = int(((x2 - x1) / unpad_w) * img.shape[1])
                y1 = max(int(((y1 - pad_y // 2) / unpad_h) * img.shape[0]), 0)
                x1 = max(int(((x1 - pad_x // 2) / unpad_w) * img.shape[1]), 0)
                _x1 = x1
                _y1 = y1
                _box_w = box_w
                _box_h = box_h
                # tao co so sanh
                _flag = 0
                # print(_boxes)
                for i in _boxes:
                    if i[0] == int(obj_id):
                        if i[1] <= _x1 and i[2] <= _y1 and i[1] + i[3] >= _x1 + _box_w and i[2] + i[4] >= _y1 + _box_h:
                            x1 = i[1]
                            y1 = i[2]
                            box_w = i[3]
                            box_h = i[4]
                            _flag = 1
                        break
                if _flag == 0:
                    # tang chieu cao chieu rong Box hơp lý
                    if box_h >= box_w:
                        _h = int(box_h / 6)
                        box_h = box_h + int(box_h / 2)
                        _w = int(box_w / 3)
                        box_w = box_w + int(box_w * 2 / 3)
                        y1 = max(int(y1 - _h), 0)
                        x1 = max(int(x1 - _w), 0)
                    else:
                        _h = int(box_h * 3 / 8)
                        box_h = box_h + int(box_h * 3 / 4)
                        _w = int(box_w / 4)
                        box_w = box_w + int(box_w / 2)
                        y1 = max(int(y1 - _h), 0)
                        x1 = max(int(x1 - _w), 0)
                    # neus chiefu cao lớn hơn 2 lần chiều rộng mở rộng gấp đ
                    if _box_h >= 2 * _box_w:
                        _h = int(_box_h / 8)
                        box_h = _box_h + int(_box_h / 3)
                        _w = int(_box_w / 2)
                        box_w = _box_w + int(_box_w)
                        x1 = max(int(_x1 - _w), 0)
                        y1 = max(int(_y1 - _h), 0)
                boxes.append([int(obj_id), x1, y1, box_w, box_h])
                person = frame[y1:box_h + y1, x1:box_w + x1].copy()
                _frame[y1:box_h + y1, x1:box_w + x1] = person
                dem +=1
        # Hoi Phuc
        for i in _boxes:
            flag = 0
            for j in boxes:
                if i[0] == j[0]:
                    flag = 1
                    break
            if flag == 0:
                boxes.append(i)
                x1 = i[1]
                y1 = i[2]
                box_w = i[3]
                box_h = i[4]
                person = frame[y1:box_h + y1, x1:box_w + x1].copy()
                _frame[y1:box_h + y1, x1:box_w + x1] = person
        _boxes = boxes.copy()
        FRAME.append(_frame)
    return FRAME


def funget10F(frames: list):
    result = []
    result.append(frames[0])
    result.append(frames[1])
    result.append(frames[3])
    result.append(frames[6])
    result.append(frames[9])
    result.append(frames[12])
    result.append(frames[15])
    result.append(frames[18])
    result.append(frames[21])
    result.append(frames[23])
    return result

# Test Model
# def fun_RealTime(URL_VIDEO):
#     #camera
#     cap = cv2.VideoCapture(URL_VIDEO)
#     isContinute, frame = cap.read()
#     if not isContinute:
#         cv2.destroyAllWindows()
#     #dieu kien
#     while True:
#         isContinute, frame = cap.read()
#         _frame = Remove_backgournd_RealTime(frame)
#         cv2.imshow('frame', _frame)
#
#         if cv2.waitKey(10) & 0xFF == ord('q'):
#             break
#     cv2.destroyAllWindows()

def predict(frame,modelVGG16,modelLSTM):
    transfer = cf.fun_getTransferValue_EDIT(pathVideoOrListFrame=frame, modelVGG16=modelVGG16)

    pre, real = libs.fun_predict(
        modelLSTM=modelLSTM,
        transferValue=transfer,
        isPrint=True
    )
    if real > 0.65:
        return print(cf.VIDEO_NAMES_DETAIL[pre])

# Real Time Camera
def fun_RealTime_Model(URL_VIDEO):
    # cv2.CAP_DSHOW
    cap = cv2.VideoCapture(URL_VIDEO)
    isContinute, frame = cap.read()
    if not isContinute:
        cv2.destroyAllWindows()
    modelVGG16 = cf.fun_getVGG16Model()
    modelLSTM = cf.fun_loadModelLSTM()
    while True:
        _25 = []
        count = 0
        while count <= 24:
            _25.append(frame)
            isContinute, frame = cap.read()
            if not isContinute:
                break
            count += 1

        if len(_25) <= 24:
            break
        #lay 25F di remove
        _RM = []
        _RM = Remove_backgournd_RealTime(_25)

        #show video
        # libs.fun_showVideo(source=_RM)

        # predict
        predict(_RM,modelVGG16,modelLSTM)
        isContinute, frame = cap.read()
        if not isContinute:
            break
    cv2.destroyAllWindows()


# Remove Backgound URL
def Remove_Backgound(URL_VIDEO):
    frames = 0
    _boxes = []
    vid = cv2.VideoCapture(URL_VIDEO)
    fourcc = cv2.VideoWriter_fourcc(*'MJPG')
    outvideo = cv2.VideoWriter(URL_VIDEO.replace('L','T'),fourcc,20.0,(224,224))
    # outvideo = cv2.VideoWriter(URL_VIDEO.replace('L','T'),fourcc,)
    mot_tracker = Sort()
    while(True):
        ret, frame = vid.read()
        print(frame)
        print(frame)
        if not ret:
            break
        frames += 1
        frame = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
        pilimg = Image.fromarray(frame)
        detections = detect_image(pilimg)
        frame = cv2.cvtColor(frame, cv2.COLOR_RGB2BGR)
        img = np.array(pilimg)
        pad_x = max(img.shape[0] - img.shape[1], 0) * (img_size / max(img.shape))
        pad_y = max(img.shape[1] - img.shape[0], 0) * (img_size / max(img.shape))
        unpad_h = img_size - pad_y
        unpad_w = img_size - pad_x
        boxes = []
        _frame = frame.copy()
        _frame *= 0
        if detections is not None:
            tracked_objects = mot_tracker.update(detections.cpu())
            unique_labels = detections[:, -1].cuda().unique()
            n_cls_preds = len(unique_labels)
            person = None
            _x1 = None
            _y1 = None
            _box_w = None
            _box_h = None
            dem = 0
            for x1, y1, x2, y2, obj_id, n_cls_preds in tracked_objects:
                if int(n_cls_preds) != 0:
                    continue
                box_h = int(((y2 - y1) / unpad_h) * img.shape[0])
                box_w = int(((x2 - x1) / unpad_w) * img.shape[1])
                y1 = max(int(((y1 - pad_y // 2) / unpad_h) * img.shape[0]),0)
                x1 = max(int(((x1 - pad_x // 2) / unpad_w) * img.shape[1]),0)
                _x1 = x1
                _y1 = y1
                _box_w = box_w
                _box_h = box_h
                # tao co so sanh
                _flag = 0
                print(boxes)
                for i in _boxes:
                    if i[0] == int(obj_id):
                        if i[1] <= _x1 and i[2] <= _y1 and i[1] + i[3] >= _x1 + _box_w and i[2] + i[4] >= _y1 + _box_h:
                            x1 = i[1]
                            y1 = i[2]
                            box_w = i[3]
                            box_h = i[4]
                            _flag = 1
                        break
                if _flag == 0:
                # tang chieu cao chieu rong Box hơp lý
                    if box_h >= box_w:
                        _h = int(box_h / 6)
                        box_h = box_h + int(box_h / 2)
                        _w = int(box_w /3)
                        box_w = box_w + int(box_w * 2/3)
                        y1 = max(int(y1 - _h),0)
                        x1 = max(int(x1 - _w),0)
                    else:
                        _h = int(box_h * 3/8)
                        box_h = box_h + int(box_h * 3/4)
                        _w = int(box_w / 4)
                        box_w = box_w + int(box_w / 2)
                        y1 = max(int(y1 - _h),0)
                        x1 = max(int(x1 - _w),0)
                    #neus chiefu cao lớn hơn 2 lần chiều rộng mở rộng gấp đ
                    if _box_h >= 2 * _box_w:
                        _h = int(_box_h / 8)
                        box_h = _box_h + int(_box_h / 3)
                        _w = int(_box_w / 2)
                        box_w = _box_w + int(_box_w)
                        x1 = max(int(_x1 - _w),0)
                        y1 = max(int(_y1 - _h),0)
                boxes.append([int(obj_id), x1, y1, box_w, box_h])
                person = frame[y1:box_h + y1, x1:box_w + x1].copy()
                _frame[y1:box_h + y1, x1:box_w + x1] = person
                dem +=1
        # Hoi Phuc
        for i in _boxes:
            flag= 0
            for j in boxes:
                if i[0] == j[0]:
                    flag=1
                    break
            if flag == 0:
                boxes.append(i)
                x1 = i[1]
                y1 = i[2]
                box_w = i[3]
                box_h = i[4]
                person = frame[y1:box_h + y1, x1:box_w + x1].copy()
                _frame[y1:box_h + y1, x1:box_w + x1] = person
        _boxes = boxes.copy()
        outvideo.write(_frame)

def fun_getVideoLabelNames_EachFolder(path: str):
    fileNames = libs.fun_getFileNames(path=path)
    for file in fileNames:
        Remove_Backgound(path + '/' + file)
    return


if __name__ == "__main__":
    # fun_RealTime_Model(0)
    # fun_RealTime_Model('D:/DESKTOP/Clip_224x224_7240clip_V7/na/L32_0053.avi')
    # Remove_Backgound('C:/Users/KHOA/Desktop/data_v1/da/da_[out_large_139].avi')
    # Remove_Backgound('D:/DESKTOP/Test/Test/L22_0001.avi')
    fun_getVideoLabelNames_EachFolder('C:/Users/KHOA/Desktop/data_v1/xt')
    # fun_getVideoLabelNames_EachFolder('C:/Users/KHOA/Desktop/data_v2/da,dn ')#xem lại da có vấn đề
    # fun_getVideoLabelNames_EachFolder('C:/Users/KHOA/Desktop/data_v1/dn')




# viet mot func param url video xong co url thi goi func Remove_Backgound




























