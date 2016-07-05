%I = imread('test_images/houses_text.png');
I = imread('test_images/lincoln_masked.png');
mask = detect_marking(I);

subplot(1, 2, 1);
w = imshow(I);
subplot(1, 2, 2);
imshow(mask);
set(w, 'AlphaData', ~mask)