using System;
using System.Collections.Generic;
using System.Text;
using System.Drawing;


// This is a Shot Detector using a threshold on the Structural Similarity index (SSIM).
// SSIM was introduced by Z. Wang, A. C. Bovik, et all. 
// in "Image quality assessment: From error visibility to structural similarity," IEEE Transactions on Image Processing, vol. 13, no. 4, pp. 600-612, Apr. 2004.
// The SSIM metric will be computed on the Y-component using a sliding window approach

namespace DxPlay
{
    class SSIMSD : BasicShotDetectionAlgorithm
    {
        public const int ALGONR = 5;
        List<double> parameters;

        public int getAlgoNr()
        {
            return ALGONR;
        }

        public List<double> getParameters()
        {
            return parameters;
        }

        int regionSize;
        double threshold;

        public SSIMSD(double threshold, int regionSize)
        {
            this.threshold = threshold;
            this.regionSize = regionSize; // aka windowsize

            parameters = new List<double>();
            parameters.Add(threshold);
            parameters.Add(regionSize);
        }

        public Boolean isCut(Bitmap previousFrame, Bitmap currentFrame)
        {
            int width = currentFrame.Width;
            int height = currentFrame.Height;

            double SSIM = 0;
            int nr_comp = 0;

            for (int x = 0; x < width - regionSize; ++x)
            {
                for (int y = 0; y < height - regionSize; ++y)
                {
                    // find means
                    double mean_previous = 0;
                    double mean_current = 0;

                    for (int i = 0; i < regionSize; ++i)
                    {
                        for (int j = 0; j < regionSize; ++j)
                        {
                            Color currPixel = currentFrame.GetPixel(x+i, y+j);
                            Color prevPixel = previousFrame.GetPixel(x+i, y+j);

                            mean_previous += prevPixel.GetBrightness();
                            mean_current += currPixel.GetBrightness();
                        }
                    }

                    double var_previous = 0.0;
                    double var_current = 0.0;
                    double var_cross = 0.0;

                    for (int i = 0; i < regionSize; ++i)
                    {
                        for (int j = 0; j < regionSize; ++j)
                        {
                            Color currPixel = currentFrame.GetPixel(x + i, y + j);
                            Color prevPixel = previousFrame.GetPixel(x + i, y + j);

                            var_previous += (mean_previous - prevPixel.GetBrightness()) * (mean_previous - prevPixel.GetBrightness());
                            var_current += (mean_current - currPixel.GetBrightness()) * (mean_current - currPixel.GetBrightness());
                            var_cross += (mean_previous - prevPixel.GetBrightness()) * (mean_current - currPixel.GetBrightness());
                        }
                    }

                    double mean_previous_squared = mean_previous * mean_previous;
                    double mean_current_squared = mean_current * mean_current;
                    double var_previous_squared = var_previous * var_previous;
                    double var_current_squared = var_current * var_current;

                    double c1 = 0.01;  // to stabilize division
                    double c2 = 0.03;

                    SSIM += ((2*mean_current*mean_previous+c1)*(2*var_cross+c2))/((mean_current_squared + mean_previous_squared + c1)*(var_current_squared+var_previous_squared +c2));
                    nr_comp++;
                }
            }

        // The structural similarity is the mean structural similarity over all sliding windows
        SSIM = SSIM / nr_comp;
        Console.WriteLine("SSIM: " + SSIM.ToString());
        return SSIM <= threshold;
        }
    }
}
