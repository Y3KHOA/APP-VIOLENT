import tkinter as tk
from tkinter.ttk import Scrollbar, Treeview, Progressbar
from tkinter.scrolledtext import ScrolledText
from Modules import LSTM_Config as cf
from Modules import PublicModules as libs
import os
import time 

PATH_SAVE_DETECTION = 'FileOutput/Detection'
LEN_ID_TREE_ITEM = 6

class TreeActionDetection:
    def __init__(self, containerFather):
        self.containerFather = containerFather
        self.PATH_SAVE_DETECTION = PATH_SAVE_DETECTION
        tree= Treeview(self.containerFather)
        tree["columns"]=("one")
        tree.column("#0", stretch=tk.NO)
        tree.column("one", stretch=tk.NO)

        tree.heading("#0",text="Name",anchor=tk.W)
        tree.heading("one", text="Date modified",anchor=tk.W)

        tree.grid(row=0, column= 0, sticky= 'nsew')
        self.tree = tree
        vsb = Scrollbar(self.containerFather, orient="vertical", command=tree.yview)
        vsb.grid(row=0, column= 1, sticky= 'nsew')
        self.containerFather.grid_rowconfigure(0, weight=0)
        self.containerFather.grid_columnconfigure(0, weight= 9)
        self.containerFather.grid_columnconfigure(1, weight= 1)

        self.tree.configure(yscrollcommand=vsb.set)

        self.containerFather.grid_rowconfigure(0, weight= 1)
        self.containerFather.grid_columnconfigure(0, weight= 1)

        self.fun_load16Folder()

        # Expand toan bo treee view
        self.open_children(self.tree.focus())

        # set double clicked
        self.tree.bind('<Double-1>', self.fun_onDoubleClicked)

    def fun_onDoubleClicked(self, event):
        item = self.tree.selection()[0]
        value = self.tree.item(item, 'text')
        if len(item) == 6:
            return
        dirs = value[0:2]
        dirs = PATH_SAVE_DETECTION + '/' + dirs + '/' + value
        libs.fun_showVideo(source= dirs, title= value)

    def fun_load16Folder(self):
        self.arr16Folder = []
        for id in range(0, len(cf.VIDEO_NAMES)):
            folName = cf.VIDEO_NAMES[id]
            fol = self.tree.insert("", id, self.fun_getID_FolderTree(fol= folName), text="Folder {0}".format(folName.upper()))
            self.arr16Folder.append(fol)

    def fun_getID_FolderTree(self, fol:str):
        return 'fol_{0}'.format(fol)

    def fun_saveVideoDetection(self, frames:list, fol:str, bonusFormThread:str = ''):
        folName = self.fun_getID_FolderTree(fol= fol)
        conv, time = libs.fun_getCurrentTime()
        fileSave = fol + '_' + conv + '_' + bonusFormThread if bonusFormThread != '' else fol + '_' +  conv

        # AVI extention default
        # fileSave += '.mp4'
        fileSave += '.avi'

        # make folder if not exists
        self.fun_makeFolder(fol= fol)

        # Save clip to disk
        pathSave = self.PATH_SAVE_DETECTION + '/' + fol
        isSave = libs.fun_saveFramesToVideo(frames= frames, path= pathSave + '/' + fileSave)

        if not isSave:
            libs.fun_print(name= 'Save video: '+ pathSave + '/' + fileSave, value= 'Error: UNKNOW ERROR')

        # Show into GUI
        self.tree.insert(folName, "end", "", text= fileSave, values=(time))
        return fileSave
    def fun_saveImgDetection(self, frames:list, fol:str, bonusFormThread:str = ''):
        folName = self.fun_getID_FolderTree(fol= fol)
        conv, time = libs.fun_getCurrentTime()
        fileSave = fol + '_' + conv + '_' + bonusFormThread if bonusFormThread != '' else fol + '_' +  conv

        # AVI extention default
        # fileSave += '.mp4'
        fileSave += '.jpg'

        # make folder if not exists
        self.fun_makeFolder(fol= fol)

        # Save clip to disk
        pathSave = self.PATH_SAVE_DETECTION + '/' + fol
        isSave = libs.fun_saveImgDetection(frames= frames[0], path= pathSave + '/' + fileSave)
        if not isSave:
            libs.fun_print(name= 'Save video: '+ pathSave + '/' + fileSave, value= 'Error: UNKNOW ERROR')

        # Show into GUI
        self.tree.insert(folName, "end", "", text= fileSave, values=(time))
        return fileSave

    def open_children(self, parent):
        self.tree.item(parent, open=True)
        for child in self.tree.get_children(parent):
            self.open_children(child)

    def fun_makeFolder(self, fol):
        path = self.PATH_SAVE_DETECTION + '/' + fol
        if not os.path.exists(path= path):
            os.makedirs(name= path)
            