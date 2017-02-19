import matplotlib.pyplot as plt
from numpy import absolute, sum, zeros, add
from skimage.filters import roberts
from skimage.io import imread
from skimage.transform import resize, rotate
from skimage.viewer import ImageViewer

SIZE = (40, 40)


def read_image(file_path):
    return imread(file_path)


def rotate_img(img, degree):
    return rotate(img, degree)


def resize_image(image, preserve_range=False):
    return resize(image, SIZE, mode='constant', preserve_range=preserve_range)


###########################################################################################
# These functions are not being used in our best solution but have been used in the past. #
###########################################################################################
def create_flattened_image(img):
    return img.flatten()


def edge_detection(image):
    edge_roberts = roberts(image)
    fig, ax0 = plt.subplots()

    ax0.imshow(edge_roberts, cmap=plt.cm.gray)
    ax0.set_title('Roberts Edge Detection')
    ax0.axis('off')

    plt.tight_layout()


def calculate_distance(image, train_image):
    diff = absolute(image - train_image)

    return sum(diff)


# Compute an average image from images
def compute_avg_image(images):
    avg = zeros((SIZE, 3), 'int')
    for img in images:
        avg = add(avg, img)
    avg //= len(images)
    return avg


def view(image):
    viewer = ImageViewer(image)
    viewer.show()
