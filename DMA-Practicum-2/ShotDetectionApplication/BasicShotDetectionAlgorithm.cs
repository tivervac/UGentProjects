using System;
using System.Collections.Generic;
using System.Text;
using System.Drawing;

namespace DxPlay
{
    interface BasicShotDetectionAlgorithm
    {
        Boolean isCut(Bitmap previousFrame, Bitmap currentFrame);
        int getAlgoNr();
        List<double> getParameters();
    }
}
