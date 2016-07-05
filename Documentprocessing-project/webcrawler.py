from html.parser import HTMLParser
from urllib.request import urlopen
from collections import deque
from time import sleep
import threading
import sys

ENCODING = 'iso-8859_15'
SLEEP = 0.1


# This class parses pages for new hyperlinks
class PageParser(HTMLParser):
    def __init__(self, url_frontier, fetched, web_graph, dictionary, text):
        super().__init__()
        self.url_frontier = url_frontier
        self.fetched = fetched
        self.web_graph = web_graph
        self.dictionary = dictionary
        self.text = text
        # True is we're in an <a> tag
        self.in_tag = False
        # True if we're in the body
        self.body_found = False
        self.link = ''

    # Add links
    def handle_starttag(self, tag, attrs):
        if tag == 'body':
            self.body_found = True
        elif (tag == 'a' and attrs[0][0] == 'href'):
            self.in_tag = True
            self.link = attrs[0][1]
            parent = peek(self.fetched)[0]
            if self.link not in self.web_graph:
                self.url_frontier.append(self.link)
                self.web_graph[self.link] = []
            # Keep track of the links on the page
            # but be sure not to add it twice
            # (this happens if a page has multiple
            # links to the same page)
            if self.link not in self.web_graph[parent]:
                self.web_graph[parent].append(self.link)

    def handle_data(self, data):
        parent = peek(self.fetched)[0]
        if self.in_tag:
            if data not in self.dictionary:
                self.dictionary[data] = [parent, self.link]
            else:
                if parent not in self.dictionary[data]:
                    self.dictionary[data].append(parent)
                if self.link not in self.dictionary[data]:
                    self.dictionary[data].append(self.link)
        # Add plain text so we can search for them
        elif self.body_found:
            data = data.split('\n')[1]
            if data == '':
                return
            if parent not in self.text:
                self.text[parent] = [data]
            elif data not in self.text[parent]:
                self.text[parent].append(data)

    def handle_endtag(self, tag):
        if tag == 'a':
            self.in_tag = False
            self.body_found = False


def peek(deque):
    try:
        return deque[0]
    except:
        print('Something\'s wrong with the program...',
              file=sys.stderr)
        exit(1)


def download_pages(base_url, url_frontier, fetched):
    while (url_frontier or fetched):
        if not url_frontier:
            # Yield
            sleep(0)
            continue

        current_page = peek(url_frontier)
        # print('Fetching ' + current_page + '...')
        try:
            html = urlopen(base_url + current_page).read().decode(ENCODING)
        except IOError:
            print("IOError while fetching " + current_page + '!',
                  file=sys.stderr)
            exit(2)

        if not html:
            print('Failed to fetch ' + current_page + '!',
                  file=sys.stderr)
            exit(3)
        # print('Fetched ' + current_page + '!')

        # Add the page to the fetched queue and remove it from the url frontier
        fetched.append((url_frontier.popleft(), html))
        # Don't overload the server
        sleep(SLEEP)


def parse_pages(url_frontier, fetched, web_graph, dictionary, text):
    parser = PageParser(url_frontier, fetched, web_graph, dictionary, text)
    while (url_frontier or fetched):
        # Parse page
        if not fetched:
            # Yield
            sleep(0)
            continue
        parser.feed(peek(fetched)[1])
        fetched.popleft()


def crawl(url):
    start_link = url.split('/')[-1]
    base_url = url[:len(url) - len(start_link)]

    threads = []
    # Contains the links to pages to fetch
    url_frontier = deque([start_link])
    # Contains the html pages and links we've fetched
    fetched = deque()
    # Containes the pages we've already visited
    # and which pages it links to
    web_graph = {start_link: []}
    dictionary = dict()
    text = dict()

    # Downloading thread
    threads.append(threading.Thread(
        target=download_pages,
        args=(base_url, url_frontier, fetched)))
    # Parsing thread
    threads.append(threading.Thread(
        target=parse_pages,
        args=(url_frontier, fetched, web_graph, dictionary, text)))

    # Start the threads
    # and make them stop when the main thread does
    for thread in threads:
        thread.daemon = True
        thread.start()

    # Keep the main thread active
    # so that we can still interrupt with ^+C
    # > 1 because the main thread is active
    while threading.active_count() > 1:
        sleep(SLEEP)

    return (dictionary, web_graph, text)
