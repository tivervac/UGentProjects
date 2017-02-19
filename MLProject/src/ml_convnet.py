import sys
import os
import time

import numpy as np

import theano
import lasagne

from nolearn.lasagne import NeuralNet

import folder_utils as fu
import image_utils as iu
import write_utils as wu

from skimage.transform import resize
from skimage.color import rgb2gray

IMGSIZE = 32

def preprocess(img):
    img = rgb2gray(img)
    img = resize(img, (IMGSIZE, IMGSIZE), mode='edge')
    img = np.array(img)
    img = np.reshape(img, (1, IMGSIZE, IMGSIZE))
    return img
  
def getfeatures(img):
    return img;

targets = list();
n_targets = 0
  
def load_dataset(path, fractions):
    data = fu.create_train_dict(os.path.join(path, 'training-data'))
    train_fraction = fractions[0]
    validation_fraction = fractions[1]
    test_fraction = fractions[2]
     
    samples = []
    
    global n_targets
    global targets
    
    # Go over all the categories
    for cat_key, cat in data.items():
        # Go over all the classes per category
        for class_key, klass in cat.items():
          
            targets.append(class_key)
            
            # Go over all the images per class
            for i, img_path in enumerate(klass):
                # Process the images
                img = iu.read_image(img_path)
                img = preprocess(img)
                features = getfeatures(img)
                samples.append((features, n_targets))
                
            n_targets = n_targets + 1

    n_samples = len(samples)
    
    print("# samples: {}".format(n_samples))
    print("# targets: {}".format(n_targets))
    
    # Shuffle the data so we don't always train/test on the same data
    np.random.shuffle(samples)
    
    n_train_samples = round(n_samples * fractions[0])
    n_validation_samples = round(n_samples * fractions[1])
    n_test_samples = round(n_samples * fractions[2])

    X_train, y_train = zip(*(samples[0 : n_train_samples]))
    X_val, y_val = zip(*(samples[n_train_samples : n_train_samples + n_validation_samples]))
    X_test, y_test = zip(*(samples[n_train_samples + n_validation_samples : n_samples]))
    
    X_train = np.array(X_train)
    X_val = np.array(X_val)
    X_test = np.array(X_test)
    
#     y_train = y_train.astype(np.uint8)
#     y_val = y_val.astype(np.uint8)
#     y_test = y_test.astype(np.uint8)
  
    print(np.shape(X_train))
    print(np.shape(y_train))
    print(np.shape(X_val))
    print(np.shape(y_val))
    print(np.shape(X_test))
    print(np.shape(y_test))
  
    return list(X_train), list(y_train), list(X_val), list(y_val), list(X_test), list(y_test)

# Test the model on a validation set
def test_model(nn, samples, keys):
    pass
  
def build_mlp(input_var=None):
    l_in = lasagne.layers.InputLayer(shape=(None, 1, IMGSIZE, IMGSIZE), input_var=input_var)
    l_in_drop = lasagne.layers.DropoutLayer(l_in, p=0.2)
    l_hid1 = lasagne.layers.DenseLayer(l_in_drop, num_units=1000, nonlinearity=lasagne.nonlinearities.rectify)
    l_hid1_drop = lasagne.layers.DropoutLayer(l_hid1, p=0.5)
    l_hid2 = lasagne.layers.DenseLayer(l_hid1_drop, num_units=400, nonlinearity=lasagne.nonlinearities.rectify)
    l_hid2_drop = lasagne.layers.DropoutLayer(l_hid2, p=0.5)
    l_out = lasagne.layers.DenseLayer(l_hid2_drop, num_units=n_targets, nonlinearity=lasagne.nonlinearities.softmax)
    return l_out
  
def build_cnn(input_var=None):
    # As a third model, we'll create a CNN of two convolution + pooling stages
    # and a fully-connected hidden layer in front of the output layer.

    # Input layer, as usual:
    inputl = lasagne.layers.InputLayer(shape=(None, 1, IMGSIZE, IMGSIZE), input_var=input_var)
    
    # This time we do not apply input dropout, as it tends to work less well for convolutional layers.

    # Convolution with 32 5x5 kernels, and 2x2 pooling:
    convl1 = lasagne.layers.Conv2DLayer(inputl, num_filters=32, filter_size=(5, 5), nonlinearity=lasagne.nonlinearities.rectify)
    subl1 = lasagne.layers.MaxPool2DLayer(convl1, pool_size=(2, 2))

    # Convolution with 32 5x5 kernels, and 2x2 pooling:
    convl2 = lasagne.layers.Conv2DLayer(subl1, num_filters=32, filter_size=(5, 5), nonlinearity=lasagne.nonlinearities.rectify)
    subl2 = lasagne.layers.MaxPool2DLayer(convl2, pool_size=(2, 2))
    
    multiscale = lasagne.layers.ConcatLayer([lasagne.layers.FlattenLayer(subl1), lasagne.layers.FlattenLayer(subl2)])
    
    dropl1 = lasagne.layers.dropout(multiscale, p=.5)
    full1 = lasagne.layers.DenseLayer(dropl1, num_units=n_targets * 20, nonlinearity=lasagne.nonlinearities.rectify)
    
    # A fully-connected layer of units with 50% dropout on its inputs:
    dropl2 = lasagne.layers.dropout(full1, p=.5)
    outputl = lasagne.layers.DenseLayer(dropl2, num_units=n_targets, nonlinearity=lasagne.nonlinearities.softmax)

    return outputl
  
def iterate_minibatches(inputs, targets, batchsize, shuffle):
    assert len(inputs) == len(targets)
    inputs = np.array(inputs)
    targets = np.array(targets)
    
    if shuffle:
      
        indices = np.arange(len(inputs))
        np.random.shuffle(indices)
        
        for start_idx in range(0, len(inputs), batchsize):
          
            if start_idx + batchsize <= len(inputs):
                selection = indices[start_idx : start_idx + batchsize]
            else:
                selection = indices[start_idx : len(inputs)]
                
            yield inputs[selection].tolist(), targets[selection].tolist()
            
    else:
      
        for start_idx in range(0, len(inputs), batchsize):
            
            if start_idx + batchsize <= len(inputs):
                selection = slice(start_idx, start_idx + batchsize)
            else:
                selection = slice(start_idx, len(inputs))
          
            yield inputs[selection].tolist(), targets[selection].tolist()
        
  
def train_model(path, fractions=(0.6, 0.2, 0.2), num_epochs=100):
    X_train, y_train, X_val, y_val, X_test, y_test = load_dataset(path, fractions)
    
    input_var = theano.tensor.tensor4('inputs')
    target_var = theano.tensor.ivector('targets')

    # network = build_mlp(input_var)
    network = build_cnn(input_var)
    
    prediction = lasagne.layers.get_output(network)
    loss = lasagne.objectives.categorical_crossentropy(prediction, target_var)
    loss = loss.mean()
    
    params = lasagne.layers.get_all_params(network, trainable=True)
    updates = lasagne.updates.nesterov_momentum(loss, params, learning_rate=0.01, momentum=0.9)
    
    test_prediction = lasagne.layers.get_output(network, deterministic=True)
    test_loss = lasagne.objectives.categorical_crossentropy(test_prediction, target_var)
    test_loss = test_loss.mean()
    
    test_acc = theano.tensor.mean(theano.tensor.eq(theano.tensor.argmax(test_prediction, axis=1), target_var), dtype=theano.config.floatX)
    
    train_fn = theano.function([input_var, target_var], loss, updates=updates)
    val_fn = theano.function([input_var, target_var], [test_loss, test_acc])
    
    result_fn = theano.function([input_var], test_prediction)
    
    for epoch in range(num_epochs):
        # In each epoch, we do a full pass over the training data:
        train_err = 0
        train_batches = 0
        start_time = time.time()
        for batch in iterate_minibatches(X_train, y_train, 500, shuffle=True):
            inputs, targets = batch
            train_err += train_fn(inputs, targets)
            train_batches += 1

        # And a full pass over the validation data:
        val_err = 0
        val_acc = 0
        val_batches = 0
        for batch in iterate_minibatches(X_val, y_val, 500, shuffle=False):
            inputs, targets = batch
            err, acc = val_fn(inputs, targets)
            val_err += err
            val_acc += acc
            val_batches += 1

        # Then we print the results for this epoch:
        print("Epoch {} of {} took {:.3f}s".format(epoch + 1, num_epochs, time.time() - start_time))
        print("  training loss:\t\t{:.6f}".format(train_err / train_batches))
        print("  validation loss:\t\t{:.6f}".format(val_err / val_batches))
        print("  validation accuracy:\t\t{:.2f} %".format(val_acc / val_batches * 100))
        
    test_err = 0
    test_acc = 0
    test_batches = 0
    for batch in iterate_minibatches(X_test, y_test, 500, shuffle=False):
        inputs, targets = batch
        err, acc = val_fn(inputs, targets)
        test_err += err
        test_acc += acc
        test_batches += 1
    print("Final results:")
    print("  test loss:\t\t\t{:.6f}".format(test_err / test_batches))
    print("  test accuracy:\t\t{:.2f} %".format(test_acc / test_batches * 100))
    
    return result_fn

def determine_result(nnmodel, path):
    data = fu.list_all_test_data(os.path.join(path, 'test'))

    test_samples = []
    
    for img_path in data:
        # Process the images
        img = iu.read_image(img_path)
        img = preprocess(img)
        features = getfeatures(img)
        test_samples.append(features)

    print(np.shape(test_samples))
    test_results = nnmodel(test_samples)
    print(np.shape(test_results))

    return test_results

if __name__ == '__main__':
    if len(sys.argv) != 2:
        print('USAGE: python %s <project_root>' % sys.argv[0], file=sys.stderr)
        exit(1)

    project_root = sys.argv[1]
    nnmodel = train_model(project_root, (0.8, 0.19, 0.01), 100)
    results = determine_result(nnmodel, project_root)
    print(results)
    wu.create_csv(results, targets, root=project_root)
