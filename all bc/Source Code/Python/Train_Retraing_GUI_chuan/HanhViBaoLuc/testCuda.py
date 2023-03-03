
from __future__ import absolute_import, division, print_function, unicode_literals

# import numpy as np
#
import tensorflow as tf

print("Version: ", tf.__version__)
print("GPU is", "available" if tf.config.experimental.list_physical_devices(
    "GPU") else "NOT AVAILABLE")

# import  os
# os.environ[‘CUDA_VISIBLE_DEVICES’] = ‘-1’


while(True):
            ret, frame = vid.read()
            if not ret:
                break
            frames += 1
            frame = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
            pilimg = Image.fromarray(frame)
            detections = detect_image(pilimg)
            frame = cv2.cvtColor(frame, cv2.COLOR_RGB2BGR)
            img = np.array(pilimg)
            pad_x = max(img.shape[0] - img.shape[1], 0) * (img_size / max(img.shape))
            pad_y = max(img.shape[1] - img.shape[0], 0) * (img_size / max(img.shape))
            unpad_h = img_size - pad_y
            unpad_w = img_size - pad_x
            boxes = []
            _frame = frame.copy()
            # backgound set black
            _frame *= 0

            # backgound set white
            # for i in range(0, len(frame)):
            #     _frame[i] = 255

            if detections is not None:
                tracked_objects = mot_tracker.update(detections.cpu())
                unique_labels = detections[:, -1].cpu().unique()
                n_cls_preds = len(unique_labels)
                person = None
                _x1 = None
                _y1 = None
                _box_w = None
                _box_h = None

                dem = 0

                for x1, y1, x2, y2, obj_id, n_cls_preds in tracked_objects:
                    # set Person
                    if int(n_cls_preds) != 0:
                        continue

                    box_h = int(((y2 - y1) / unpad_h) * img.shape[0])
                    box_w = int(((x2 - x1) / unpad_w) * img.shape[1])
                    y1 = max(int(((y1 - pad_y // 2) / unpad_h) * img.shape[0]),0)
                    x1 = max(int(((x1 - pad_x // 2) / unpad_w) * img.shape[1]),0)

                    _x1 = x1
                    _y1 = y1
                    _box_w = box_w
                    _box_h = box_h
                    # set flag
                    _flag = 0
                    for i in _boxes:
                        if i[0] == int(obj_id):
                            if i[1] <= _x1 and i[2] <= _y1 and i[1] + i[3] >= _x1 + _box_w and i[2] + i[4] >= _y1 + _box_h:
                                x1 = i[1]
                                y1 = i[2]
                                box_w = i[3]
                                box_h = i[4]
                                _flag = 1
                            break
                    if _flag == 0:
                    # set wight, height box
                        if box_h >= box_w:
                            _h = int(box_h / 6)
                            box_h = box_h + int(box_h / 2)
                            _w = int(box_w /3)
                            box_w = box_w + int(box_w * 2/3)
                            y1 = max(int(y1 - _h),0)
                            x1 = max(int(x1 - _w),0)
                        else:
                            _h = int(box_h * 3/8)
                            box_h = box_h + int(box_h * 3/4)
                            _w = int(box_w / 4)
                            box_w = box_w + int(box_w / 2)
                            y1 = max(int(y1 - _h),0)
                            x1 = max(int(x1 - _w),0)
                        # if height>2 else wigth*2
                        if _box_h >= 2 * _box_w:
                            _h = int(_box_h / 8)
                            box_h = _box_h + int(_box_h / 3)
                            _w = int(_box_w / 2)
                            box_w = _box_w + int(_box_w)
                            x1 = max(int(_x1 - _w),0)
                            y1 = max(int(_y1 - _h),0)
                    boxes.append([int(obj_id), x1, y1, box_w, box_h])
                    person = frame[y1:box_h + y1, x1:box_w + x1].copy()
                    _frame[y1:box_h + y1, x1:box_w + x1] = person
                    dem +=1
            # recuperate Frames
            for i in _boxes:
                flag= 0
                for j in boxes:
                    if i[0] == j[0]:
                        flag=1
                        break
                if flag == 0:
                    boxes.append(i)
                    x1 = i[1]
                    y1 = i[2]
                    box_w = i[3]
                    box_h = i[4]
                    person = frame[y1:box_h + y1, x1:box_w + x1].copy()
                    _frame[y1:box_h + y1, x1:box_w + x1] = person

            _boxes = boxes.copy()
            outvideo.write(_frame)
