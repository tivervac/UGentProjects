import sys
from math import log
from os.path import join
from random import shuffle

import numpy as np
import pandas
from skimage.color import rgb2gray, rgb2hsv
from skimage.exposure import histogram, equalize_hist
from skimage.feature import hog, local_binary_pattern
from sklearn.ensemble import AdaBoostClassifier
from sklearn.grid_search import GridSearchCV
from sklearn.linear_model import SGDClassifier
from sklearn.neural_network import MLPClassifier
from sklearn.metrics import log_loss
from skimage.transform import integral_image, resize
from sklearn.pipeline import make_pipeline
from sklearn.preprocessing import RobustScaler
from sklearn.feature_selection import VarianceThreshold
from sklearn.metrics import log_loss

import folder_utils as fu
import image_utils as iu
import write_utils as wu
from haar_features import haar_feature
from Models import Models


def preprocess(img):
    return iu.resize_image(img, True)


def create_hog(img):
    arr0 = hog(img, orientations=8, pixels_per_cell=(5, 5), cells_per_block=(2, 2), visualise=False, normalise=False)
    #arr1 = hog(img, orientations=9, pixels_per_cell=(4, 4), cells_per_block=(2, 2), visualise=False, normalise=False)
    return arr0 #np.append(arr0, arr1)


def create_lbp(img):
    lbp = local_binary_pattern(img, 10, 8)
    return lbp.flatten()


def create_haar(img):
    return haar_feature(img, 15, 30, 1, 1)

HIST_BINS = 16
def create_histogram(img):
    img = rgb2hsv(img)
    h = histogram(img[:, :, 0], HIST_BINS)
    # s = histogram(img[:, :, 1], HIST_BINS)[0]

    """"
    no_of_max = 4
    h = np.array(h)
    # Get the indices of the no_of_max maximum values in h
    ind = np.argpartition(h, -no_of_max)[-no_of_max:]
    # Sort them from index of highest value to lowest
    ind = h[ind[np.argsort(h[ind])]][::-1]
    return ind"""
    return h

def get_features(img):
    #flattened_image = resize(img, (10,10), mode='constant', preserve_range=False).flatten()
    rgb_img = img
    img = equalize_hist(rgb2gray(img), nbins=48)
    features = np.append(create_lbp(img), create_hog(img))
    return features

def select_features(images, threshold = (0.8 * (1 - 0.8))):
    selector = VarianceThreshold(threshold)
    selector.fit(images)
    selection = selector.transform(images)
    return selection, selector

# def filter_features(images, mask):
#     images = np.array(images)
#     mask = np.array(mask)
#     selection = images[:, mask]
#     return selection
  
def scale_features(images):
    scaler = RobustScaler()
    scaler.fit(images)
    images = scaler.transform(images)
    return images, scaler

def create_datasets(feature_function, path, test_fraction, double):
    train = fu.create_train_dict(join(path, 'training-data'))
    images = []
    class_keys = []
    test_images = []
    test_class_keys = []

    # Iterate over all images
    for cat_key, cat in train.items():
        for class_key, klass in cat.items():
            # Determine what parts will be used for validation and which for testing
            split_index = len(klass) * (1 - test_fraction)
            # Shuffle the data so we don't always train/test on the same data
            if test_fraction != 0:
                shuffle(klass)

            # Go over all the images per class
            for i, img_path in enumerate(klass):
                # Process the images
                img = iu.read_image(img_path)
                img = preprocess(img)
                features = feature_function(img)

                # Check if we want to double the dataset by rotating every image
                if double:
                    img2 = iu.rotate_img(img, -10)
                    features2 = feature_function(img2)

                # Save the images and their class name
                if i < split_index:
                    images.append(features)
                    class_keys.append(class_key)
                    if double:
                        images.append(features2)
                        class_keys.append(class_key)
                else:
                    test_images.append(features)
                    test_class_keys.append(class_key)
                    if double:
                        test_images.append(features2)
                        test_class_keys.append(class_key)

    # Transform lists to numpy arrays for easier usage later on
    images = np.array(images)
    class_keys = np.array(class_keys)

    return images, class_keys, test_images, test_class_keys


# Test the model on a validation set
def test_model(model, images, keys):
    print(len(images))
    print(len(keys))
  
    length = len(images)
    test_keys = keys
    pred_count = 0

    print(np.shape(images))
    
    predictions = model.predict(images)
    
    # Predict the classes of the validation set
    for i in range(length - 1):
        prediction = predictions[i]
        key = test_keys[i]
        
        if prediction == key:
            pred_count += 1

    # Print some results about the predictions on the validation set
    print("Predicted %d/%d traffic signs correctly" % (pred_count, length))
    # print(pandas.crosstab(np.array(test_keys), np.array(predict_keys)))
    
    # Print the logloss, still having some trouble with this
    y = model.predict_proba(images)
    print("The builtin logloss is: %0.3f" % log_loss(keys, y))


def train_cv_model(images, keys):
    clf = SGDClassifier()
    parameters = {'loss': ('log', 'modified_huber'), 'penalty': ('l2', 'l1')}

    grid_search = GridSearchCV(clf, parameters, n_jobs=-1, verbose=1)
    grid_search.fit(images, keys)

    return grid_search.best_estimator_, grid_search.best_score_


def train_neural_network(images, class_keys):
#    clf = MLPClassifier(activation='logistic', hidden_layer_sizes=(100,50))
#    parameters = {'algorithm': ('adam', 'sgd', 'l-bfgs'),'activation':('logistic',), 'alpha': (1e-4,1e-3,1e-2), 'batch_size':(100,200),
#                  'learning_rate':('constant', 'adaptive'), 'hidden_layer_sizes':((100,50),)}
    mlp = MLPClassifier(activation='logistic', algorithm='adam', alpha=0.01,
         batch_size=100, beta_1=0.9, beta_2=0.999, early_stopping=False,
         epsilon=1e-08, hidden_layer_sizes=(100, 50),
         learning_rate='adaptive', learning_rate_init=0.001, max_iter=100,
         momentum=0.9, nesterovs_momentum=True, power_t=0.5,
         random_state=None, shuffle=True, tol=0.01,
         validation_fraction=0.1, verbose=False, warm_start=False)
    mlp.fit(images, class_keys)
    return mlp, mlp.score(images, class_keys)
#    grid_search = GridSearchCV(clf, parameters, n_jobs=-1, verbose=1)
#    grid_search.fit(images, class_keys)

#    print(grid_search.best_estimator_)
#    print(grid_search.best_estimator_.get_params())
#    return grid_search.best_estimator_, grid_search.best_score_


def pipeline(images, class_keys):
    scaler = RobustScaler()
    clf = SGDClassifier(loss='log', penalty='l2')
    neural = MLPClassifier(activation='logistic', algorithm='sgd', hidden_layer_sizes=(100, 50))
    pl = make_pipeline(scaler, neural)

    pl.fit(images, class_keys)

    return pl, pl.score(images, class_keys)


def train_ada_model(images, keys):
    clf = SGDClassifier(loss='log')

    input_path = sys.argv[1]
    feature_fn = get_features
    trained_model = train_neural_network(input_path, feature_fn)
    result = determine_result(trained_model, input_path, feature_fn)
    wu.create_csv(result, keys=trained_model.classes_)
    abc = AdaBoostClassifier(clf, n_estimators=50, algorithm='SAMME')
    abc.fit(images, keys)

    return abc, abc.score(images, keys)


def train_model(path, feature_function, test_fraction=0.0, double=False, method=Models.MLP):
    images, class_keys, test_images, test_class_keys = create_datasets(feature_function, path, test_fraction, double)
    
    [images, scaler] = scale_features(images)
    [images, selector] = select_features(images)

    model_function = {
        Models.CV: train_cv_model,
        Models.ADA: train_ada_model,
        Models.MLP: train_neural_network,
        Models.PIPE: pipeline
    }
    
    # Train the appropriate model
    function = model_function.get(method)

    if function is None:
        print("Invalid model found", file=sys.stderr)
        exit(2)

    model, score = function(images, class_keys)
    
    print("Best score: %0.3f" % score)
    
    # Test the validation set if there is one
    if test_fraction != 0:
        test_images = scaler.transform(test_images)
        test_images = selector.transform(test_images)
        test_model(model, test_images, test_class_keys)

    return model, scaler, selector


def determine_result(model, scaler, selector, path, feature_function):
    test_images = fu.list_all_test_data(join(path, 'test'))

    images = []
    # Create the features for the images to test
    for img_path in test_images:
        img = iu.read_image(img_path)
        img = preprocess(img)
        features = feature_function(img)
        images.append(features)

    images = scaler.transform(images)
    images = selector.transform(images)
        
    # Predict the classes of the images
    body = model.predict_proba(np.array(images))
    
    return body


if __name__ == '__main__':
    if len(sys.argv) != 2:
        print('USAGE: python %s <project_root>' % sys.argv[0], file=sys.stderr)
        exit(1)

    project_root = sys.argv[1]
    feature_fn = get_features
    [trained_model, scaler, selector] = train_model(project_root, feature_fn, 0.2)
    result = determine_result(trained_model, scaler, selector, project_root, feature_fn)
    wu.create_csv(result, trained_model.classes_, root=project_root)


def calc_logloss(test_set, class_labels, predictions):
    n = len(test_set)
    m = len(class_labels)
    logloss = 0
    for i in range(0, n):
        for j in range(0, m):
            y = int(class_labels[np.argmax(predictions[i])] == class_labels[j])
            pij = max(min(predictions[i][j], 1 - (10 ^ -15)), 10 ^ 15)
            logloss += y * log(pij)

    logloss /= -n
    return logloss
