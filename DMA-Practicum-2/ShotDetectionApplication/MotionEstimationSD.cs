using System;
using System.Collections.Generic;
using System.Text;
using System.Drawing;

namespace DxPlay
{
    class MotionEstimationSD : BasicShotDetectionAlgorithm
    {
        private double threshold;
        private int blockSize, searchWindowSize;
        private List<double> parameters;
        private const int ALGONR = 2;

        public int getAlgoNr()
        {
            return ALGONR;
        }

        public List<double> getParameters()
        {
            return parameters;
        }

        public MotionEstimationSD(double threshold, int blockSize, int searchWindowSize)
        {
            this.threshold = threshold;
            this.blockSize = blockSize;
            this.searchWindowSize = searchWindowSize;

            parameters = new List<double>();
            parameters.Add(threshold);
            parameters.Add((double)blockSize);
            parameters.Add((double)searchWindowSize);
        }

        public Boolean isCut(Bitmap prev, Bitmap curr)
        {
            int xBlocks = curr.Width / blockSize;
            int yBlocks = curr.Height / blockSize;
            double totalDistance = 0;
            double[,] previousValues = new double[curr.Width, curr.Height];
            double[,] currentValues = new double[curr.Width, curr.Height];

            // Calculate the pixel values in before hand
            for (int i = 0; i < curr.Height; i++)
            {
                for (int j = 0; j < curr.Width; j++)
                {
                    previousValues[j, i] = RGBToLuminance(prev.GetPixel(j, i));
                    currentValues[j, i] = RGBToLuminance(curr.GetPixel(j, i));
                }
            }

            // Previous frame
            for (int i = 0; i < yBlocks; i++)
            {
                for (int j = 0; j < xBlocks; j++)
                {
                    double minimalDistance = Double.MaxValue;
                    int starty = ((i * blockSize) - searchWindowSize) > 0 ? (i * blockSize) - searchWindowSize : 0;
                    int endy = ((i * blockSize) + searchWindowSize) > prev.Height ? prev.Height : (i * blockSize) + searchWindowSize;
                    int startx = ((j * blockSize) - searchWindowSize) > 0 ? (j * blockSize) - searchWindowSize : 0;
                    int endx = ((j * blockSize) + searchWindowSize) > prev.Width ? prev.Width : (j * blockSize) + searchWindowSize;

                    // Current frame with searchwindow
                    for (int k = starty / blockSize; k < Math.Ceiling((double) (endy / blockSize)); k++)
                    {
                        for (int l = startx / blockSize; l < Math.Ceiling((double) (endx / blockSize)); l++)
                        {
                            double distance = 0;
                            // Minimize the block difference
                            for (int m = 0; m < blockSize; m++)
                            {
                                for (int n = 0; n < blockSize; n++)
                                {
                                    int x = m + (j * blockSize);
                                    int y = n + (i * blockSize);
                                    int x1 = m + (l * blockSize);
                                    int y1 = n + (k * blockSize);

                                    if (x < startx / blockSize || x >= Math.Ceiling((double) (endx / blockSize)) ||
                                        y < starty / blockSize || y >= Math.Ceiling((double) (endy / blockSize))) 
                                    { 
                                        continue; 
                                    }
                                    // Calculate the values of the pixels, for this we use the Y Luminance
                                    distance += Math.Abs(previousValues[x, y] - currentValues[x1, y1]);
                                }
                            }

                            if (distance < minimalDistance)
                            {
                                minimalDistance = distance;
                            }
                        }
                    }
                    totalDistance += minimalDistance;
                }
            }

            return totalDistance > threshold;
        }

        private double RGBToLuminance(Color pixel)
        {
            return 0.299 * pixel.R + 0.597 * pixel.G + 0.114 * pixel.B;
        }
    }
}
