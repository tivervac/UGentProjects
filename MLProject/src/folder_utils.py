from os import walk, path
from os.path import normpath, basename, join


# Lists all the png images in file_path
def list_all_data(file_path):
    files = []
    for p, d, f in walk(file_path):
        for file in f:
            if file.endswith(".png"):
                files.append(path.join(p, file))
    return files


def list_all_train_data(file_path=join('..', 'training-data')):
    return list_all_data(file_path)


def list_all_test_data(file_path=join('..', 'test')):
    return list_all_data(file_path)


def create_train_dict(file_path=join('..', 'training-data')):
    """
    Create a dictionary with the location of all training data files
    :param file_path: location to the training data files
    :return: dictionary with all training data
    """
    d = dict()
    for f in list_all_train_data(file_path):
        # type: sign form factor
        # sign: sign number from the road code
        # file_name: name of png file
        _, _, cat, sign, file_name = f.split('/')
        t = d.get(cat, dict())
        s = t.get(sign, list())
        s.append(f)
        t[sign] = s
        d[cat] = t
    return d


###########################################################################################
# These functions are not being used in our best solution but have been used in the past. #
###########################################################################################
def get_basename(file_path):
    return basename(normpath(file_path))


# Creates a mapping of classes to superclasses (= cat)
def create_class_cat_mapping():
    d = create_train_dict()
    cat_map = {}
    for k, v in d.items():
        for cl in v.keys():
            cat_map[cl] = k
    return cat_map
