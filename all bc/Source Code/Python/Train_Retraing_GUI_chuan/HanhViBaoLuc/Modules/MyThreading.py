import json
import time
import cv2
import threading
from PIL import Image
from PIL import ImageTk
from Modules import PublicModules as libs
from Modules import LSTM_Config as cf
import random
# from HanhViBaoLuc import RealTime_URL as RL
import RealTime_URL as RL
from datetime import date
import pyrebase
import os




class MyThreadingVideo:
    def __init__(self, lbShow, lbFather, lbShowKetQua, vgg16_model, lstm_model, treeAction):
        self.vgg16_model = vgg16_model
        self.lstm_model = lstm_model
        self.treeAction = treeAction
        self.frames = None
        self._RM = None
        self._10 = None
        self.lbShow = lbShow
        self.lbShowKetQua = lbShowKetQua
        self.lbFather = lbFather
        self.myThread = threading.Thread(target=self.VideoThread)
        self.myThread.setDaemon(True)
###
    def setFrames(self, frames: list):
        self.frames = frames
        # Tinh chinh Frame phu hop trong day
        self._10 = RL.funget10F(self.frames)
        self._RM =  RL.Remove_backgournd_RealTime(self._10)
###
    def isBusy(self):
        return self.myThread.isAlive()

    def startShowVideo(self):
        self.myThread = threading.Thread(target=self.VideoThread)
        self.myThread.setDaemon(True)
        self.myThread.start()
    def VideoThread(self): # hàm trả về kết quả
        # # Predict cho moi 20Frames Anh tai day
        transfer = cf.fun_getTransferValue_EDIT(pathVideoOrListFrame= self._RM, modelVGG16= self.vgg16_model)
        pre, real = libs.fun_predict(modelLSTM= self.lstm_model, transferValue=transfer)
        conv = cf.VIDEO_NAMES_DETAIL[pre]# trả về mảng và dùng softmax lấy những thằng lớn nhất
        if real > 0.6 and conv != 'no': # biến lớn nhất trên... nếu > 0.6 thì đưa ra hành động, còn ko thì = no
            text = 'Predict: {0} [ {1:.4f} ]'.format(conv, real)
        else:
            text = ''
        if self.lbShow is not None:
            # Show thread video
            for frame in self.frames:
                image = libs.fun_cv2_imageArrayToImage(containerFather= self.lbFather, frame= frame.copy(), reSize= 0.8)
                self.lbShow.config(image= image)
                self.lbShow.image = image
        self.lbShowKetQua.config(text= text)

        if text != '':
            print('Day la hanh dong========9999: ',text)
            print('Day là thoi gian========9999:', date.today())
            fileSave = self.treeAction.fun_saveVideoDetection(frames= self._RM, fol= cf.VIDEO_NAMES[pre])
            print('đây à dường dan video========9999: ', fileSave)
            # D:\khoa\HanhViBaoLuc_AThang\Train_Retraing_GUI_chuan\HanhViBaoLuc\FileOutput\Detection\ + hanh dong + fileSave
            pathVideoName =  'D:/khoa/HanhViBaoLuc_AThang/Train_Retraing_GUI_chuan/HanhViBaoLuc/FileOutput/Detection/' + cf.VIDEO_NAMES[pre] + '/' + fileSave
            print('đây à dường dan video tuyet doi ========9999: ', pathVideoName)

        #setup firebase
        d_config={
            "apiKey": "AIzaSyBV2GIk_j0RkJ7003LMwUPcVUuD9RLK_I0",
            "authDomain": "demopython-ef257.firebaseapp.com",
            "databaseURL": "https://demopython-ef257-default-rtdb.firebaseio.com/",
            "projectId": "demopython-ef257",
            "storageBucket": "demopython-ef257.appspot.com",
            "serviceAccount": "Modules/serviceAccountKey.json"
        }

        firebase_storage= pyrebase.initialize_app(d_config)
        storage= firebase_storage.storage()


        #up len firebase
        import os

        # def convert_avi_to_mp4(avi_file_path):
        #     output_name = ''
        #     os.popen(
        #         "ffmpeg -i '{input}' -ac 2 -b:v 2000k -c:a aac -c:v libx264 -b:a 160k -vprofile high -bf 0 -strict experimental -f mp4 '{output}.mp4'".format(
        #             input=avi_file_path, output=output_name))
        #     return output_name

        # import os

        # def convert_avi_to_mp4(avi_file_path, output_name):
        #     os.popen(
        #         "ffmpeg -i '{input}' -ac 2 -b:v 2000k -c:a aac -c:v libx264 -b:a 160k -vprofile high -bf 0 -strict experimental -f mp4 '{output}.mp4'".format(
        #             input=avi_file_path, output=output_name))
        #     return output_name

        import moviepy.editor as moviepy


        video_avi ='D:/khoa/HanhViBaoLuc_AThang/Train_Retraing_GUI_chuan/HanhViBaoLuc/FileOutput/Detection/' + cf.VIDEO_NAMES[pre] + '/' + fileSave

        print('13241')
        print(fileSave)



        arr = fileSave.split('.')
        # print(arr[0])
        clip= moviepy.VideoFileClip(video_avi)
        clip.write_videofile('D:/khoa/HanhViBaoLuc_AThang/Train_Retraing_GUI_chuan/HanhViBaoLuc/FileOutput/Detection/'+ cf.VIDEO_NAMES[pre]
                             + '/'+arr[0]+'.mp4')
        video_mp4 = 'D:/khoa/HanhViBaoLuc_AThang/Train_Retraing_GUI_chuan/HanhViBaoLuc/FileOutput/Detection/'+ cf.VIDEO_NAMES[pre] \
                    + '/'+arr[0]+'.mp4'

        a=arr[0]+'.mp4'

        f = cv2.VideoCapture(video_mp4)

        currentframe=0

        # while (currentframe==0):
        # reading from frame

        rval, frame = f.read()
        name = 'D:/khoa/HanhViBaoLuc_AThang/Train_Retraing_GUI_chuan/HanhViBaoLuc/FileOutput/IMG/' + \
               cf.VIDEO_NAMES[pre] + '/' + arr[0] + '.jpg'
        s= cv2.imwrite(name, frame)
        print(s)
        f.release()


# writing the extracted images
            # cv2.imshow(frame)
            # increasing counter so that it will
            # show how many frames are created
            # currentframe += 1
        # else:
        #     break


        # b= convert_avi_to_mp4(video_avi,a)


        # abc= 'abc'+random(10000000)

        storage.child('Detection/' + cf.VIDEO_NAMES[pre] + '/' + arr[0]+'.mp4').put(video_mp4,token='abc')# up file
        storage.child('DetectionIMG/' + cf.VIDEO_NAMES[pre] + '/' + arr[0]+'.jpg').put(name,token='abc')# up file

        t=storage.child('Detection/' + cf.VIDEO_NAMES[pre] + '/' + arr[0]+'.mp4').get_url(token='abc')
        t1=storage.child('DetectionIMG/' + cf.VIDEO_NAMES[pre] + '/' + arr[0]+'.jpg').get_url(token='abc')

        print(t)# bốc ra token

        #download
        # storage.child('Detection/' + 'no' + '/' + 'no_2022_11_17T21_13_04_230736.mp4').download('C:/Users/KHOA/Desktop/dddddddddddddddd.mp4')
        # storage.child('Detection/ma/ma_2022_10_04T17_12_06_456443.avi').download('C:/Users/KHOA/Desktop/dddddddddddddddd')

        #crete file json


        from datetime import datetime
        now = datetime.now()
        current_time = now.strftime(
                                    "%d/%m/%Y %H:%M:%S")
        database = firebase_storage.database();
        data= {
            'link': str(t),
            'time': current_time,
            'name':arr[0],
            'urlImage': str(t1)
        }

        # db = firebase_storage.database()

        # data = {"Age": 21, "Name": "Jenna", "Employed": True}
        # -------------------------------------------------------------------------------
        # Create Data

        # db.push(data)
        # db.child("Users").child("FirstPerson").set(data)
        database.child('Detection').child(cf.VIDEO_NAMES[pre]).child(arr[0]).set(data)

        # from firebase import firebase
        # firebase = firebase.FirebaseApplication("https://demopython-ef257-default-rtdb.firebaseio.com/", None)
        # stringurl = "/Detection/" +cf.VIDEO_NAMES[pre]
        # print(stringurl)
        # print(data)
        # result = firebase.post(stringurl,params=data)
        # print(result)
        # import firebase_admin
        # from firebase_admin import credentials
        # from firebase_admin import firestore
        #
        # # if not firebase_admin._apps:
        # #     cred = credentials.Certificate("D:/khoa/HanhViBaoLuc_AThang/Train_Retraing_GUI_chuan/HanhViBaoLuc/serviceAccountKey.json")
        # # firebase_admin.initialize_app(cred)
        #
        #
        # cred = credentials.Certificate(d_config)
        # print('963852741')
        # print(cred)
        #
        # db= firestore.client()
        # db.collection('Detection/'+cf.VIDEO_NAMES[pre]).add({'url':t})


        # with open('D:/khoa/HanhViBaoLuc_AThang/Train_Retraing_GUI_chuan/HanhViBaoLuc/output/detec.json','w') as f:
        #     json.dump(detec,f)



