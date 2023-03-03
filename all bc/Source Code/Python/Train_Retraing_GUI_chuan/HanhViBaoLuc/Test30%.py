from Modules import LSTM_Config as cf

if __name__ == '__main__':
    '''
        LOAD VGG16 MODEL,
        - Hien tai minh dung VGG16 model cua keras trong so = imagenet
    '''
    modelVGG16 = cf.fun_getVGG16Model()

    '''
        LAY TOAN BO VIDEO VA NHAN CUA TUNG VIDEO,
        - Truy cap vao thu muc DIR_INPUT_TRAIN lay toan bo file video va nhan bo vao mang.
        - Vidu: names[da1_1_111_001.avi,nt2_1_222_002.avi] ~ labels[ [1,0,0], [0,1,0] ]
    '''

    names, labels = cf.fun_getVideoLabelNames_EachFolder(path=cf.DIR_INPUT_TRAIN)

    '''
        CHUAN BI TAP DU LIEU & NHAN (lable) DE TRAIN LSTM,
        - Mang LSTM duoc dinh nghia nhan vao 20 frame hinh,
        Moi hinh duoc cho qua VGG16 de lay mau ~ 4096
        - Vidu: 20 frame (224 x 224) ~ 20 * 4096 = [ [4096PhanTu],... [4096PhanTu] ]
    '''
    ####  neu chua train co VGG16 mo cai nay ra

    # trainSet, labelSet = cf.fun_getTrainSet_LabelSet_SaveFile(pathVideoOrListFrame=cf.DIR_INPUT_TRAIN
    #                                                         , numItem=len(names), modelVGG16= modelVGG16, names= names, labels= labels)

    #### train roi co VGG16 mo ra chua Train dong lai
    # Load File Saved of Data after throw VGG16 Model
    trainSet, labelSet = cf.fun_getTrainSet_LabelSet_LoadFile(numItem=len(names))


    print('total [train, vald, test]: ', len(trainSet))
    input('any: ')

    NUM_TESTS = int(len(trainSet) * cf.TEST_PERCENT)

    # Init Test Set
    testSet = trainSet[0:NUM_TESTS]
    testLabelSet = labelSet[0:NUM_TESTS]

    modelLSTM = cf.fun_getModelLSTM_5(num_classify=cf.NUM_CLASSIFY)
    modelLSTM.summary()


    print('Len Test: ' + str(len(testSet)))
    input('any: ')

    '''
        DU DOAN % DO CHINH XAC,
        - Thu muc test tai: Data/Test/
    '''
    ## Load Model de du doan
    modelLSTM.load_weights('Modules/LSTM_Model_18PL_25F_7024clip_21_03_21.h5')

    cf.fun_evaluate(modelLSTM=modelLSTM, testSet=testSet, testLabelSet=testLabelSet)
