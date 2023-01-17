#!/usr/bin/python

# date   2021-12-18

import os
from tqdm import tqdm
import requests
from bs4 import BeautifulSoup
from concurrent.futures.thread import ThreadPoolExecutor
from fake_useragent import UserAgent # 这个包是随机UA,需要安装,也可以不用这个


def url_to_response(links, do_headers):
    response = requests.get(links, headers=do_headers)
    response.encoding = 'utf-8'
    # response.encoding = response.apparent_encoding
    return response.text


def url_body_to_content_list(html_string):
    content_urls = []
    soup = BeautifulSoup(html_string, 'lxml')
    book_ul_lists = soup.find_all('li')
    for book_ul in book_ul_lists:
        url_soup = book_ul.find('a')
        urls = url_soup['href']
        number = url_soup['href'].split('/')[-1].split('.')[0]
        title = url_soup['title']
        content = number + " " + title + "," + urls
        content_urls.append(content)
    return content_urls


def content_list_to_files(file_path, *args):
    for a in args:
        with open(file_path, 'a', encoding='utf-8') as file_object:
            file_object.write(a + '\n')


def get_chapter_urls_list(name):
    list_book = []
    with open(name, 'r', encoding='utf-8') as file_object:
        lines = file_object.readlines()
        for line in lines:
            new_line = line.strip()
            list_book.append(new_line)
    return list_book


def get_chapter_content_to_files(book_path, chapter_url, do_headers):
    download_url = chapter_url.split(',')[1]
    rep = url_to_response(download_url, do_headers)
    soup = BeautifulSoup(rep, 'lxml')
    content_list = soup.find('div', id='neirong').find_all('p')
    for content in content_list:
        with open(book_path + "/" + chapter_url.split(',')[0] + ".txt", 'a', encoding='utf-8') as file_w:
            file_w.write(content.text + '\n\n')


if __name__ == '__main__':
    url = 'https://www.51shucheng.net/kehuan/santi/santi3'  # 定义章节列表页
    path = '小说/三体3' # 存储目录
    ua = UserAgent()
    if not os.path.exists(path):
        os.mkdir(path)
    tmp_file = path + "/tmp.txt"
    headers = {'User-Agent': ua.random}
    responses = url_to_response(url, headers)
    content_url_list = url_body_to_content_list(responses)
    content_list_to_files(tmp_file, *content_url_list)
    chapter_list = get_chapter_urls_list(tmp_file)
    with ThreadPoolExecutor(20) as t:
        for book in tqdm(chapter_list):
            t.submit(get_chapter_content_to_files, path, book, headers)
