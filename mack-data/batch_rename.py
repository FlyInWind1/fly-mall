#!/usr/bin/env python
# encoding: utf-8
import glob
import os

import sys

path_all = sys.argv[1]

# 开始数字
start_num = sys.argv[2]

# 结束数字，需要比实际看到的+1 比如10-20 需要range(10,21)
end_num = sys.argv[3]

reduce_num = int(sys.argv[4])

last_cata = int(sys.argv[4])

origin_path_list = glob.glob(path_all)

# origin_path_list = glob.glob('/data/306/*/')
origin_path_list.sort()
print(origin_path_list)

for origin_all_path in origin_path_list:
    origin_all_path_split = origin_all_path.split("/")
    origin_file_name = origin_all_path[origin_all_path.rfind("/") + 1:len(origin_all_path)]
    print(origin_file_name)
    origin_directory = origin_all_path[0:origin_all_path.rfind("/") + 1]
    origin_file_name_split = origin_file_name.split("$")
    img_page_no = origin_file_name_split[2]
    path_replace_pre = origin_file_name_split[0] + "$" + origin_file_name_split[1] + "$"
    print(origin_file_name + "========path_suf=====")
    print(img_page_no + "=======path_jpg======")
    print(path_replace_pre + "=======path_replace_pre======")
    if start_num <= img_page_no <= end_num:
        print("replace**********")
        path_jpg_num_replace = int(img_page_no) - reduce_num

        if path_jpg_num_replace <= 0:
            path_jpg_num_replace = path_jpg_num_replace + 9000

        path_jpg_replace = str(path_jpg_num_replace).zfill(4)

        replace_all_path = origin_directory + path_replace_pre + path_jpg_replace

        replace_path_suf = replace_all_path[replace_all_path.rfind("/") + 1:len(replace_all_path)]

        before_pdf = origin_all_path + "/" + origin_file_name + ".pdf"
        after_pdf = origin_all_path + "/" + replace_path_suf + ".pdf"
        print("=====before_pdf====")
        print(before_pdf)
        print("=====after_pdf====")
        print(after_pdf)

        os.rename(before_pdf, after_pdf)

        print("=====beforepath====")
        print(origin_all_path)
        print("=====afterpath====")
        print(replace_all_path)

        os.rename(origin_all_path, replace_all_path)
