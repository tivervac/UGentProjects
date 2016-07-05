using System;
using System.Collections.Generic;
using System.Text;
using System.Drawing;


namespace DxPlay
{

    class PixelDifferenceSD : BasicShotDetectionAlgorithm
    {
        private double firstThreshold, secondThreshold;
        private List<double> parameters;
        private const int ALGONR = 1;

        public int getAlgoNr()
        {
            return ALGONR;
        }

        public List<double> getParameters()
        {
            return parameters;
        }

        public PixelDifferenceSD(double firstThreshold, double secondThreshold)
        {
            this.firstThreshold = firstThreshold;
            this.secondThreshold = secondThreshold;

            parameters = new List<double>();
            parameters.Add(firstThreshold);
            parameters.Add(secondThreshold);
        }

        public Boolean isCut(Bitmap prev, Bitmap curr)
        {
            int width = curr.Width;
            int height = curr.Height;

            int totalDistance = 0;
            for (int x = 0; x < width; ++x)
            {
                for (int y = 0; y < height; ++y)
                {
                    int distance = 0;
                    Color currPixel = curr.GetPixel(x, y);
                    Color prevPixel = prev.GetPixel(x, y);
                    distance += Math.Abs(currPixel.R - prevPixel.R);
                    distance += Math.Abs(currPixel.G - prevPixel.G);
                    distance += Math.Abs(currPixel.B - prevPixel.B);

                    distance = (distance > secondThreshold) ? 1 : 0;
                    totalDistance += distance;
                }
            }

            return totalDistance > firstThreshold;
        }
    }
}
