using System;
using System.Collections.Generic;
using System.Text;
using System.Drawing;

namespace DxPlay
{
    class GlobalHistogramSD : BasicShotDetectionAlgorithm
    {
        private double threshold;
        private int bins;
        private int size;
        private List<double> parameters;
        private const int ALGONR = 3;

        public int getAlgoNr()
        {
            return ALGONR;
        }

        public List<double> getParameters()
        {
            return parameters;
        }

        public GlobalHistogramSD(double threshold, int bins)
        {
            this.threshold = threshold;
            this.bins = bins;
            this.size = (int)Math.Ceiling((3 * 256 + 2 * 256 + 256) / (double)bins);

            parameters = new List<double>();
            parameters.Add(threshold);
            parameters.Add(bins);
        }

        public Boolean isCut(Bitmap prev, Bitmap curr)
        {
            int[] previousHistogram = new int[bins];
            int[] currentHistogram = new int[bins];

            // Fill the histograms
            for (int i = 0; i < prev.Height; i++)
            {
                for (int j = 0; j < prev.Width; j++)
                {
                    int prevValue = (int)(RGBToUniqueValue(prev.GetPixel(j, i)) / size);
                    int currValue = (int)(RGBToUniqueValue(curr.GetPixel(j, i)) / size);
                    previousHistogram[prevValue]++;
                    currentHistogram[currValue]++;
                }
            }

            int distance = 0;
            for (int i = 0; i < bins; i++)
            {
                distance += Math.Abs(previousHistogram[i] - currentHistogram[i]);
            }

            return distance > threshold;
        }

        private double RGBToUniqueValue(Color pixel)
        {
            return (512 + pixel.R) + (256 + pixel.G) + pixel.B + 0.99;
        }
    }
}
