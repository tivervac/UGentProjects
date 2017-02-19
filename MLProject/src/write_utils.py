import os

import numpy as np
import pandas as pd

from folder_utils import create_train_dict, list_all_test_data


# Creates the header for the csv
def create_header(keys):
    l = []

    for key in keys:
        l.append(key)

    return np.array(l)


def create_csv(body, keys, root='..'):
    filename = os.path.join(root, 'result.csv')
    header = create_header(keys)
    columns = [x + 1 for x in range(len(body))]

    df = pd.DataFrame(body, index=columns, columns=header)
    df.to_csv(filename, index=True, index_label='Id', header=True, sep=',')


###########################################################################################
# These functions are not being used in our best solution but have been used in the past. #
###########################################################################################
def calculate_prior_probabilities():
    d = create_train_dict()
    probs = dict()
    total = 0

    for val in d.values():
        for k, v in val.items():
            probs[k] = len(v)
            total += len(v)

    for k in probs.keys():
        probs[k] /= total

    return probs


def create_probs_matrix():
    probs = calculate_prior_probabilities()

    line = np.empty(shape=(1, len(probs)))
    for i, prob in enumerate(probs.values()):
        line[0, i] = prob

    arr = np.tile(line, (len(list_all_test_data()), 1))

    return arr


def create_prob_csv():
    body = create_probs_matrix()
    create_csv(body)
