import pagerank
import query
import sys
import pickle
import os.path

FILENAME = 'statistics.txt'


def print_usage(name):
    print('{}: USAGE: [make|query] [url|terms]'.format(name))


def validate_args():
    if (len(sys.argv) < 2
       or (sys.argv[1] != 'make'
       and sys.argv[1] != 'query')
       or(sys.argv[1] == 'make'
       and len(sys.argv) > 3)
       or (sys.argv[1] == 'query'
       and len(sys.argv) < 3)):
        print_usage(sys.argv[0])
        exit(4)


def make(url):
    dictionary, text, pr, indices = pagerank.perform_pagerank(url)

    start_link = url.split('/')[-1]
    base_url = url[:len(url) - len(start_link)]
    # Serialize our objects and write them to a file
    with open(FILENAME, 'wb') as f:
        pickler = pickle.Pickler(f)
        pickler.dump(base_url)
        pickler.dump(dictionary)
        pickler.dump(text)
        pickler.dump(pr)
        pickler.dump(indices)


def query_terms(terms):
    if not os.path.isfile(FILENAME):
        print('{} doesn\'t exist, run make first!'.format(FILENAME),
              file=sys.stderr)
        exit(5)

    # Read the serialized objects
    with open(FILENAME, 'rb') as f:
        unpickler = pickle.Unpickler(f)
        base_url = unpickler.load()
        dictionary = unpickler.load()
        text = unpickler.load()
        pr = unpickler.load()
        indices = unpickler.load()
    result = query.perform_query(dictionary, text, pr, terms, indices)

    # Add the baseurl to the links
    result = [base_url + str(i) for i in result]
    for i in range(len(result)):
      print(result[i])


validate_args()
if sys.argv[1] == 'make':
    make(sys.argv[2])
else:
    query_terms(sys.argv[2:])
