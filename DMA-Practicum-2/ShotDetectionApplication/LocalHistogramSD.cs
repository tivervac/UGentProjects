using System;
using System.Collections.Generic;
using System.Text;
using System.Drawing;

namespace DxPlay
{
    class LocalHistogramSD : BasicShotDetectionAlgorithm
    {
        private double threshold;
        private int bins;
        private int size;
        private int regionSize;
        private List<double> parameters;
        private const int BLOCKS = 9;
        private int ALGONR = 4;

        public int getAlgoNr()
        {
            return ALGONR;
        }

        public List<double> getParameters()
        {
            return parameters;
        }

        public LocalHistogramSD(double threshold, int bins, int regionSize)
        {
            this.threshold = threshold;
            this.bins = bins;
            this.size = (int)Math.Ceiling((3 * 256 + 2 * 256 + 256) / (double)bins);
            this.regionSize = regionSize;

            parameters = new List<double>();
            parameters.Add(threshold);
            parameters.Add(bins);
            parameters.Add(regionSize);
        }

        public Boolean isCut(Bitmap prev, Bitmap curr)
        {
            int[,] previousHistogram = new int[BLOCKS, bins];
            int[,] currentHistogram = new int[BLOCKS, bins];

            // Fill the histograms
            for (int b = 0; b < BLOCKS; b++)
            {
                for (int i = 0; i < regionSize; i++)
                {
                    for (int j = 0; j < regionSize; j++)
                    {
                        if (((b * regionSize) + j >= curr.Width) || ((b * regionSize) + i >= curr.Height))
                        {
                            continue;
                        }
                        int prevValue = (int)(RGBToUniqueValue(prev.GetPixel((b * regionSize) + j, (b * regionSize) + i)) / size);
                        int currValue = (int)(RGBToUniqueValue(curr.GetPixel((b * regionSize) + j, (b * regionSize) + i)) / size);

                        previousHistogram[b, prevValue]++;
                        currentHistogram[b, currValue]++;
                    }
                }
            }

            int distance = 0;
            for (int b = 0; b < BLOCKS; b++)
            {
                for (int i = 0; i < bins; i++)
                {
                    distance += Math.Abs(previousHistogram[b, i] - currentHistogram[b, i]);
                }
            }

            return distance > threshold;
        }

        private double RGBToUniqueValue(Color pixel)
        {
            return (512 + pixel.R) + (256 + pixel.G) + pixel.B + 0.99;
        }
    }
}
