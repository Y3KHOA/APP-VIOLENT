######################## config ##############################

import tensorflow as tf
from keras.applications import VGG16
from tensorflow import keras
from keras.models import Model
import numpy as np
from random import shuffle
from HanhViBaoLuc.Modules import PublicModules as lib
from keras.models import Sequential
from keras.layers import LSTM, Dense, Activation, Dropout
import matplotlib.pyplot as plt
import cv2

DIR_ROOT = ''
DIR_INPUT_TRAIN ='C:/Users/KHOA/Desktop/data_v2'
# DIR_INPUT_TRAIN ='D:/Khóa luận/Video/25F_Black.Tracking_224x224_7240clip_V7_lan2'
DIR_INPUT_TEST = DIR_ROOT + 'Data/Test'
DIR_INPUT_TEST1 = DIR_ROOT + 'Data/Test1'
DIR_INPUT_VALIDATION = DIR_ROOT + 'Data/Validation'
DIR_INPUT_SHOW_VIDEO_TEST = DIR_ROOT + 'Data/ShowVideoTest'
DIR_INPUT_SHOW_VIDEO_TRAIN = DIR_ROOT + 'Data/ShowVideoTrain'
#Dat ten fiel luu model
# DIR_MODEL_LSTM ='Modules/LSTM_Model_18PL_10F_7240clip_20_07_21.h5'
# DIR_MODEL_LSTM ='Modules/30F_07_02_2023_Good.h5'#dau vao LSTM=20 -- sai
# DIR_MODEL_LSTM ='Modules/10F_09_02_2023_Good.h5'
DIR_MODEL_LSTM ='Modules/10F_20_07_2021_Good.h5'#chuan
# DIR_TRANSFER_VALUES_VGG16_MODEL ='Modules/TransferValuesVGG16_07_02_23.npy'#dau vao LSTM=20 -- sai
# DIR_TRANSFER_VALUES_VGG16_MODEL ='Modules/TransferValuesVGG16_09_02_23.npy'
DIR_TRANSFER_VALUES_VGG16_MODEL ='Modules/TransferValuesVGG16.npy' #chuan
SIZE = (224, 224)
# NUM_FRAME_INPUT_LSTM = 20
NUM_FRAME_INPUT_LSTM = 10
TRANSFER_VALUE_SIZE = 4096
RNN_SIZE = 600
DENSE1 = 1024
DENSE2 = 70
EPOCH = 900
BATCH_SIZE = 300
LEARNING_RATE = 0.0001
# So Luong Validation
VALID_PERCENT = 0.2
# % Du lieu de test
TEST_PERCENT = 0.3
# K-Folder Validation
K_FOLD = 10
# Lay Model de Tran lai
# o day ạ thày đổi tên ở dây cua model cần traing lại
# fileMODEL ='Modules/LSTM_Model_17PL_25F_4839clip_50_20_30.h5'



VIDEO_NAMES = [
    'bc',
    'cq',
    'da',
    'dn',
    'kc',
    'lg',
    'lk',
    'na',
    'nc',
    'ne',
    'nt',
    'om',
    'tc',
    'vk',
    'xd',
    'xt',
    'ma',
    'no'
]

VIDEO_NAMES_DETAIL = [
    'bc - Bóp Cổ',
    'cq - Cởi Quần Áo',
    'da - Đá, Đạp',
    'dn - Đánh, Tát',
    'kc - Kẹp Cổ',
    'lg - Lên Gối',
    'lk - Lôi Kéo',
    'na - Nằm Xuống Sàn',
    'nc - Nắm Cổ',
    'ne - Ném Đồ Vật',
    'nt - Nắm Tóc',
    'om - Ôm, Vật Lôn',
    'tc - Thủ Thế Võ',
    'vk - Vật, Vũ Khí',
    'xd - Xô Đẩy',
    'xt - Xỉ Tay',
    'mau - Máu',
    'no - Không'  # Khong co hanh dong bao luc
]

VIDEO_LABELS = [
    [1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
    [0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
    [0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0],
    [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0],
    [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1],
    # [1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
    # [0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
    # [0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
    # [0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
    # [0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
    # [0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
    # [0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
    # [0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0],
    # [0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0],
    # [0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0],
    # [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0],
    # [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0],
    # [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0],
    # [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0],
    # [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0],
    # [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0],
    # [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1],
]

NUM_CLASSIFY = len(VIDEO_NAMES)


######################## end config ##############################

# dinh nghia VGG16 Model
def fun_getVGG16Model():
    modelCNN = VGG16(include_top=True, weights='imagenet')
    modelCNN.summary()
    transferLayer = modelCNN.get_layer(name='fc2')
    imgModelTransfer = Model(inputs=modelCNN.input, outputs=transferLayer.output)
    return imgModelTransfer


# One hot
def fun_onesHotLabel(label: list):
    _ones = np.ones([NUM_FRAME_INPUT_LSTM, NUM_CLASSIFY])
    _onesHot = label * _ones
    return np.array(_onesHot)


# Danh nhan video
def fun_getVideoLabelNames_EachFolder(path: str):
    names = []
    labels = []

    for fol in VIDEO_NAMES:
        folder = path + '/' + fol
        fileNames = lib.fun_getFileNames(path=folder)
        index = VIDEO_NAMES.index(fol)
        for file in fileNames:
            names.append('/' + fol + '/' + file)
            labels.append(VIDEO_LABELS[index])

    c = list(zip(names, labels))
    shuffle(c)

    names, labels = zip(*c)
    return names, labels


# Loc video
def fun_locVideoDuFrame(path: str):
    names, label = fun_getVideoLabelNames_EachFolder(path=path)
    incree = 1
    max = len(names)
    for file in names:
        frames = lib.fun_getFramesOfVideo_ALL(path=DIR_INPUT_TRAIN + file)
        if len(frames) < 25:
            print(file)
        lib.fun_print_process(count=incree, max=max, mess='Filter Frame Count Precess: ')
        incree += 1


# nem 20 frame hinh vao VGG16 Model
def fun_getTransferValue(pathVideoOrListFrame, modelVGG16):
    if isinstance(pathVideoOrListFrame, str):
        frames = lib.fun_getFramesOfVideo(path=pathVideoOrListFrame, count=NUM_FRAME_INPUT_LSTM)
    else:
        frames = pathVideoOrListFrame

    frames = lib.fun_resizeFrames(frames=frames, size=SIZE)

    frames = np.array(frames)
    frames = (frames / 255.).astype(np.float16)

    transfer = modelVGG16.predict(frames)
    return transfer


# nem 20 frame hinh vao VGG16 Model
def fun_getTransferValue_EDIT(pathVideoOrListFrame, modelVGG16):
    images = []
    if (isinstance(pathVideoOrListFrame, str)):
        vidcap = cv2.VideoCapture(pathVideoOrListFrame)
        success, image = vidcap.read()
        count = 0
        while count < NUM_FRAME_INPUT_LSTM:
            try:
                RGB_img = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
                res = cv2.resize(RGB_img, dsize=SIZE,
                                 interpolation=cv2.INTER_CUBIC)
                images.append(res)
                success, image = vidcap.read()
                count += 1
            except:
                break
    else:
        for image in pathVideoOrListFrame:
            try:
                RGB_img = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
                res = cv2.resize(RGB_img, dsize=SIZE,
                                 interpolation=cv2.INTER_CUBIC)
                images.append(res)
            except:
                break

    # NUM_FRAME_INPUT_LSTM Nho thay lai cai nay khi on
    # print('951')
    # print(len(images))
    if len(images) != NUM_FRAME_INPUT_LSTM:
        lib.fun_print(name='Frames count: ' + pathVideoOrListFrame, value=len(images))
        return None
    resul = np.array(images)
    resul = (resul / 255.).astype(np.float16)

    # # Pre-allocate input-batch-array for images.
    # shape = (NUM_FRAME_INPUT_LSTM,) + SIZE + (3,)

    # image_batch = np.zeros(shape=shape, dtype=np.float16)

    # image_batch = resul

    # # Pre-allocate output-array for transfer-values.
    # # Note that we use 16-bit floating-points to save memory.
    # shape = (NUM_FRAME_INPUT_LSTM, TRANSFER_VALUE_SIZE)
    # transfer_values = np.zeros(shape=shape, dtype=np.float16)

    transfer_values = modelVGG16.predict(resul)

    return transfer_values


# chuan bi tap du lieu + nhan de train lstm
def fun_getTrainSet_LabelSet(pathVideoOrListFrame: str, numItem: int, modelVGG16, names, labels, mess: str = 'Train'):
    count = 0
    trainSet = []
    labelSet = []
    while count < numItem:
        itemTrain = fun_getTransferValue_EDIT(pathVideoOrListFrame=pathVideoOrListFrame + names[count],
                                              modelVGG16=modelVGG16)
        itemLable = fun_onesHotLabel(label=labels[count])

        trainSet.append(itemTrain)
        labelSet.append(itemLable[0])

        lib.fun_print_process(count=count, max=numItem,
                              mess='Video frame throw into VGG16 Model Processing {0}: '.format(mess))

        count += 1

    return trainSet, labelSet


# chuan bi tap du lieu + nhan de train lstm
def fun_getTrainSet_LabelSet_SaveFile(pathVideoOrListFrame: str, numItem: int, modelVGG16, names, labels,
                                      mess: str = 'Train'):
    count = 0
    trainSet = []
    labelSet = []
    with open(file=DIR_TRANSFER_VALUES_VGG16_MODEL, mode='wb') as f:
        # the first write len dataset
        np.save(f, np.array(numItem))
        # write next recode of dataset
        while count < numItem:
            itemTrain = fun_getTransferValue_EDIT(pathVideoOrListFrame=pathVideoOrListFrame + names[count],
                                                  modelVGG16=modelVGG16)
            itemLable = fun_onesHotLabel(label=labels[count])

            trainSet.append(itemTrain)
            labelSet.append(itemLable[0])

            np.save(f, itemTrain)
            np.save(f, itemLable[0])

            lib.fun_print_process(count=count, max=numItem,
                                  mess='Video frame throw into VGG16 Model Processing {0}: '.format(mess))

            count += 1

    return trainSet, labelSet


# chuan bi tap du lieu + nhan de train lstm
def fun_getTrainSet_LabelSet_LoadFile(numItem: int, mess: str = 'Load File: '):
    count = 0
    trainSet = []
    labelSet = []
    with open(file=DIR_TRANSFER_VALUES_VGG16_MODEL, mode='rb') as f:
        # the first read len dataset
        np.load(f)
        # read next recode of dataset
        while count < numItem:
            itemTrain = np.load(f)
            itemLable = np.load(f)

            trainSet.append(itemTrain)
            labelSet.append(itemLable)

            lib.fun_print_process(count=count, max=numItem,
                                  mess='Video frame throw into VGG16 Model Processing {0}: '.format(mess))

            count += 1

    return trainSet, labelSet


# Dinh nghia mang LSTM
def fun_getModelLSTM(rnn_size: int = RNN_SIZE, input_shape: tuple = (NUM_FRAME_INPUT_LSTM, TRANSFER_VALUE_SIZE),
                     num_classify: int = NUM_CLASSIFY):
    modelLSTM = Sequential()
    modelLSTM.add(LSTM(rnn_size, input_shape=input_shape))
    modelLSTM.add(Dense(DENSE1))
    modelLSTM.add(Activation('relu'))
    modelLSTM.add(Dense(DENSE2))
    modelLSTM.add(Activation('sigmoid'))
    modelLSTM.add(Dense(num_classify))
    modelLSTM.add(Activation('softmax'))
    modelLSTM.compile(loss='mean_squared_error', optimizer='adam', metrics=['accuracy'])

    return modelLSTM


# Dinh nghia mang LSTM 2
def fun_getModelLSTM_2(rnn_size: int = RNN_SIZE, input_shape: tuple = (NUM_FRAME_INPUT_LSTM, TRANSFER_VALUE_SIZE),
                       num_classify: int = NUM_CLASSIFY):
    modelLSTM = Sequential()
    modelLSTM.add(LSTM(rnn_size, input_shape=input_shape))
    modelLSTM.add(Dense(DENSE1))
    modelLSTM.add(Activation('relu'))
    modelLSTM.add(Dense(DENSE2))
    modelLSTM.add(Activation('sigmoid'))
    modelLSTM.add(Dense(num_classify))
    modelLSTM.add(Activation('softmax'))

    opt = keras.optimizers.Adam(learning_rate=LEARNING_RATE)
    modelLSTM.compile(loss='mean_squared_error', optimizer=opt, metrics=['accuracy'])

    return modelLSTM

# Ổn định nhất nhừng tg train lâu kết quae đầu ra okey và accuracy cao nhất
# Dinh nghia mang LSTM 5
def fun_getModelLSTM_5(rnn_size: int = RNN_SIZE, input_shape: tuple = (NUM_FRAME_INPUT_LSTM, TRANSFER_VALUE_SIZE),
                       num_classify: int = NUM_CLASSIFY):
    modelLSTM = Sequential()
    modelLSTM.add(LSTM(1024, input_shape=input_shape))
    modelLSTM.add(Dense(200))
    modelLSTM.add(Activation('relu'))
    modelLSTM.add(Dense(50))
    modelLSTM.add(Activation('sigmoid'))
    modelLSTM.add(Dense(num_classify))
    modelLSTM.add(Activation('softmax'))

    opt = keras.optimizers.Adam(learning_rate=LEARNING_RATE)
    modelLSTM.compile(loss='mean_squared_error', optimizer=opt, metrics=['accuracy'])

    return modelLSTM

#Tốc độ train nhanh nhưng không ổn định là accuracy ra không cao
# Dinh nghia mang LSTM 6
# thử với trường hợp không giảm model thì sẽ như thế nào (Dropout)
def fun_getModelLSTM_6(rnn_size: int = RNN_SIZE, input_shape: tuple = (NUM_FRAME_INPUT_LSTM, TRANSFER_VALUE_SIZE),
                       num_classify: int = NUM_CLASSIFY):
    modelLSTM = Sequential()
    modelLSTM.add(LSTM(120, input_shape=input_shape))
    modelLSTM.add(Dense(1024, activation='relu'))
    modelLSTM.add(Dropout(.5))
    modelLSTM.add(Dense(512, activation='relu'))
    modelLSTM.add(Dropout(.5))
    modelLSTM.add(Dense(128, activation='relu'))
    modelLSTM.add(Dropout(.5))
    modelLSTM.add(Dense(64, activation='relu'))
    modelLSTM.add(Dense(NUM_CLASSIFY, activation='softmax'))

    opt = keras.optimizers.Adam(learning_rate=LEARNING_RATE)
    modelLSTM.compile(loss='mean_squared_error', optimizer=opt, metrics=['accuracy'])

    return modelLSTM


# bat dau cong viec train lstm percent
def fun_START_TRAINT_LSTM_PERCENT(modelLSTM, trainSet, labelSet):
    lenValid = int(VALID_PERCENT * len(trainSet))

    # Init Valid
    valSet = trainSet[0:lenValid]
    valLabelSet = labelSet[0:lenValid]
    # Init Train
    trainSet = trainSet[lenValid:]
    labelSet = labelSet[lenValid:]
    print('Len Validation: ' + str(len(valSet)))
    input('any: ')
    print('Len Train: ' + str(len(trainSet)))
    input('any: ')
    history = modelLSTM.fit(np.array(trainSet), np.array(labelSet), epochs=EPOCH,
                            validation_data=(np.array(valSet), np.array(valLabelSet)),
                            batch_size=BATCH_SIZE, verbose=2)
    lib.fun_print(name='LSTM Train', value='Train Finish!')
    return history


# Them model
def get_model_nameCT():
    return DIR_MODEL_LSTM

# Cai Tien K cho Train lai
def fun_START_RETRANING_LSTM_PERCENT(modelLSTM, trainSet, labelSet):

    c = list(zip(trainSet, labelSet))
    shuffle(c)
    trainSet, labelSet = zip(*c)

    lenValid = int(VALID_PERCENT * len(trainSet))

    # Init Valid
    valSet = trainSet[0:lenValid]
    valLabelSet = labelSet[0:lenValid]
    # Init Train
    trainSet = trainSet[lenValid:]
    labelSet = labelSet[lenValid:]
    print('Len Validation: ' + str(len(valSet)))
    print('Len Train: ' + str(len(trainSet)))

    # create Model Retraning
    checkpoint = tf.keras.callbacks.ModelCheckpoint(filepath=get_model_nameCT(),
                                                    monitor='val_accuracy', verbose=1,
                                                    save_best_only=True, mode='max')
    callbacks_list = [checkpoint]
    # Load model
    modelLSTM.load_weights(get_model_nameCT())

    history = modelLSTM.fit(np.array(trainSet), np.array(labelSet), epochs=EPOCH,
                            validation_data=(np.array(valSet), np.array(valLabelSet)),
                            callbacks=callbacks_list,
                            batch_size=BATCH_SIZE, verbose=2)
    lib.fun_print(name='LSTM Train', value='Train Finish!')

    return history





def get_model_name(k):
    return 'Modules/K_model_' + str(k) + '.h5'


def fun_mergeArray(arr1, arr2):
    res = []
    for x in arr1:
        res.append(x)
    for x in arr2:
        res.append(x)
    return res

#K anh Viet CHUAN
def fun_START_TRAINT_LSTM_PERCENT_K_Fold(modelLSTM, trainSet, labelSet, testSet, testLabelSet):
    history = None
    VALIDATION_ACCURACY = []
    VALIDATION_LOSS = []

    max = len(trainSet)
    index = max // K_FOLD
    for k in range(0, K_FOLD):
        start = index * k
        end = start + index

        # Anh xa validation
        _valSet = trainSet[start:end]
        _valLabelSet = labelSet[start:end]

        # Phan con lai de train
        _trainLeft = trainSet[0:start]
        _trainRight = trainSet[end:max]
        _trainFOLD = fun_mergeArray(_trainLeft, _trainRight)

        _labelLeft = labelSet[0:start]
        _labelRight = labelSet[end:max]
        _labelFOLD = fun_mergeArray(_labelLeft, _labelRight)

        print('#################### Train fold {0} | valid: {1}, train: {2} ####################'.format(k + 1, len(_valSet), len(_trainFOLD)))

        # Bat dau train
        # create callback
        checkpoint = tf.keras.callbacks.ModelCheckpoint(filepath=get_model_name(k),
                                                        monitor='val_accuracy', verbose=1,
                                                        save_best_only=True, mode='max')
        callbacks_list = [checkpoint]
        modelLSTM = None
        modelLSTM = fun_getModelLSTM_5()
        history = modelLSTM.fit(np.array(_trainFOLD), np.array(_labelFOLD), epochs=EPOCH,
                                validation_data=(np.array(_valSet), np.array(_valLabelSet)),
                                callbacks=callbacks_list,
                                batch_size=BATCH_SIZE, verbose=2)

        '''
        HIEN THI BIEU DO HOI TU
        '''
        fun_showAnalysis(history= history)

        # PLOT HISTORY# :# :# LOAD BEST MODEL to evaluate the performance of the model
        modelLSTM.load_weights(get_model_name(k))
        # evaluate
        print('#################### Train fold {0} | valid: {1}, train: {2} ####################'.format(k + 1, len(_valSet), len(_trainFOLD)))
        results = modelLSTM.evaluate(np.array(_valSet), np.array(_valLabelSet))
        results = dict(zip(modelLSTM.metrics_names, results))
        VALIDATION_ACCURACY.append(results['accuracy'])
        VALIDATION_LOSS.append(results['loss'])

        '''
            DU DOAN % DO CHINH XAC,
            - Thu muc test tai: Data/Test/
        '''
        print('\nLen test: '+ str(len(testSet)) + '\n')
        fun_evaluate(modelLSTM= modelLSTM, testSet= testSet, testLabelSet= testLabelSet)

        tf.keras.backend.clear_session()

    print(VALIDATION_ACCURACY)
    print(VALIDATION_LOSS)
    return history

# K Anh VIET TANG CAO
# def fun_START_TRAINT_LSTM_PERCENT_K_FoldCT(modelLSTM, trainSet, labelSet, testSet, testLabelSet):
#     history = None
#     VALIDATION_ACCURACY = []
#     VALIDATION_LOSS = []
#
#     max = len(trainSet)
#     index = max // K_FOLD
#     for k in range(0, K_FOLD):
#         start = index * k
#         end = start + index
#
#         # Anh xa validation
#         _valSet = trainSet[start:end]
#         _valLabelSet = labelSet[start:end]
#         # Phan con lai de train
#         _trainLeft = trainSet[0:start]
#         _trainRight = trainSet[end:max]
#         _trainFOLD = fun_mergeArray(_trainLeft, _trainRight)
#
#         _labelLeft = labelSet[0:start]
#         _labelRight = labelSet[end:max]
#         _labelFOLD = fun_mergeArray(_labelLeft, _labelRight)
#
#         lib.fun_print(name= 'Train fold {0}'.format(k), value= 'valid: {0}, train: {1}'.format(len(_valSet), len(_trainFOLD)))
#
#         # Bat dau train
#         # create callback
#         checkpoint = tf.keras.callbacks.ModelCheckpoint(filepath=get_model_name(k),
#                                                         monitor='val_accuracy', verbose=1,
#                                                         save_best_only=True, mode='max')
#         callbacks_list = [checkpoint]
#
#         history = modelLSTM.fit(np.array(_trainFOLD), np.array(_labelFOLD), epochs=EPOCH,
#                                 validation_data=(np.array(_valSet), np.array(_valLabelSet)),
#                                 callbacks=callbacks_list,
#                                 batch_size=BATCH_SIZE, verbose=2)
#
#         '''
#             HIEN THI BIEU DO HOI TU
#         '''
#         fun_showAnalysis(history=history)
#
#         '''
#             DU DOAN % DO CHINH XAC,
#             - Thu muc test tai: Data/Test/
#         '''
#         fun_evaluate(modelLSTM=modelLSTM, testSet=testSet, testLabelSet=testLabelSet)
#
#         # PLOT HISTORY# :# :# LOAD BEST MODEL to evaluate the performance of the model
#         modelLSTM.load_weights(get_model_name(k))
#
#         # evaluate
#         results = modelLSTM.evaluate(np.array(_valSet), np.array(_valLabelSet))
#         results = dict(zip(modelLSTM.metrics_names, results))
#         VALIDATION_ACCURACY.append(results['accuracy'])
#         VALIDATION_LOSS.append(results['loss'])
#         tf.keras.backend.clear_session()
#
#     print(VALIDATION_ACCURACY)
#     print(VALIDATION_LOSS)
#     return history



# bat dau cong viec train lstm
def fun_START_TRAINT_LSTM(modelVGG16, modelLSTM, trainSet, labelSet):
    valName, valLabel = fun_getVideoLabelNames_EachFolder(path=DIR_INPUT_VALIDATION)
    print('len Valid: ', len(valName))
    input('any: ')
    valSet, valLabelSet = fun_getTrainSet_LabelSet(pathVideoOrListFrame=DIR_INPUT_VALIDATION, numItem=len(valName),
                                                   modelVGG16=modelVGG16,
                                                   names=valName, labels=valLabel,
                                                   mess='Validation')
    history = modelLSTM.fit(np.array(trainSet), np.array(labelSet), epochs=EPOCH,
                            validation_data=(np.array(valSet), np.array(valLabelSet)),
                            batch_size=BATCH_SIZE, verbose=2)
    lib.fun_print(name='LSTM Train', value='Train Finish!')
    return history


# Show bieu do hoi tu
def fun_showAnalysis(history):
    plt.plot(history.history['accuracy'])
    plt.plot(history.history['val_accuracy'])
    plt.title('model accuracy')
    plt.ylabel('accuracy')
    plt.xlabel('epoch')
    plt.legend(['train', 'validation'], loc='upper left')
    plt.savefig('destination_path.eps', format='eps', dpi=1000)
    plt.show()
    # summarize history for loss
    plt.plot(history.history['loss'])
    plt.plot(history.history['val_loss'])
    plt.title('model loss')
    plt.ylabel('loss')
    plt.xlabel('epoch')
    plt.legend(['train', 'validation'], loc='upper left')
    plt.savefig('destination_path1.eps', format='eps', dpi=1000)
    plt.show()


def fun_loadModelLSTM():
    ## Dung cai nao mo cai do
    modelLSTM = fun_getModelLSTM_5(num_classify=NUM_CLASSIFY)
    modelLSTM.load_weights(filepath=DIR_MODEL_LSTM)
    return modelLSTM


def fun_evaluate(modelLSTM, testSet, testLabelSet):
    result = modelLSTM.evaluate(np.array(testSet), np.array(testLabelSet))
    for name, value in zip(modelLSTM.metrics_names, result):
        print(name, value)


def fun_FilterVideoFitFrameCount(fileName: str, count: int = 25):
    frames = lib.fun_getFramesOfVideo_ALL(path=fileName)
    if len(frames) < count:
        print(fileName)
