/****************************************************************************
While the underlying libraries are covered by LGPL, this sample is released 
as public domain.  It is distributed in the hope that it will be useful, but 
WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
or FITNESS FOR A PARTICULAR PURPOSE.  
*****************************************************************************/

using System;
using System.IO;
using System.Drawing;
using System.Drawing.Imaging;
using System.Collections;
using System.Runtime.InteropServices;
using System.Diagnostics;
using System.Threading;

using DirectShowLib;
using System.Collections.Generic;

namespace DxPlay
{
    /// <summary> Summary description for MainForm. </summary>
    internal class CaptureNullRenderer : ISampleGrabberCB, IDisposable
    {
        #region Member variables

        /// <summary> graph builder interface. </summary>
        private IFilterGraph2 m_FilterGraph = null;
        IMediaControl m_mediaCtrl = null;
        IMediaEvent m_MediaEvent = null;

        /// <summary> Dimensions of the image, calculated once in constructor. </summary>
        private int m_videoWidth;
        private int m_videoHeight;
        private int m_stride;
        private int m_Count = 0;

#if DEBUG
        // Allow you to "Connect to remote graph" from GraphEdit
        DsROTEntry m_rot = null;
#endif

        #endregion

        #region API

        [DllImport("Kernel32.dll", EntryPoint="RtlMoveMemory")]
        private static extern void CopyMemory(IntPtr Destination, IntPtr Source, [MarshalAs(UnmanagedType.U4)] uint Length);

        #endregion

        /// <summary> File name to scan</summary>
        public CaptureNullRenderer(string FileName)
        {
            try
            {
                // Set up the capture graph
                SetupGraph(FileName);
            }
            catch
            {
                Dispose();
                throw;
            }
        }
        /// <summary> release everything. </summary>
        public void Dispose()
        {
            CloseInterfaces();
        }
        // Destructor
        ~CaptureNullRenderer()
        {
            CloseInterfaces();
        }


        /// <summary> capture the next image </summary>
        public void Start()
        {
            int hr = m_mediaCtrl.Run();
            DsError.ThrowExceptionForHR( hr );
        }


        public void WaitUntilDone()
        {
            int hr;
            EventCode evCode;
            const int E_Abort = unchecked((int)0x80004004);

            do
            {
                System.Windows.Forms.Application.DoEvents();
                hr = this.m_MediaEvent.WaitForCompletion(100, out evCode);
            } while (hr == E_Abort);
            DsError.ThrowExceptionForHR(hr);
        }

        /// <summary> build the capture graph for grabber. </summary>
        private void SetupGraph(string FileName)
        {
            int hr;

            ISampleGrabber sampGrabber = null;
            IBaseFilter	baseGrabFlt = null;
            IBaseFilter capFilter = null;
            IBaseFilter nullrenderer = null;

            // Get the graphbuilder object
            m_FilterGraph = new FilterGraph() as IFilterGraph2;
            m_mediaCtrl = m_FilterGraph as IMediaControl;
            m_MediaEvent = m_FilterGraph as IMediaEvent;

            IMediaFilter mediaFilt = m_FilterGraph as IMediaFilter;

            try
            {
#if DEBUG
                m_rot = new DsROTEntry( m_FilterGraph );
#endif

                // Add the video source
                hr = m_FilterGraph.AddSourceFilter(FileName, "Ds.NET FileFilter", out capFilter);
                DsError.ThrowExceptionForHR( hr );

                // Get the SampleGrabber interface
                sampGrabber = new SampleGrabber() as ISampleGrabber;
                baseGrabFlt = sampGrabber as IBaseFilter;

                ConfigureSampleGrabber(sampGrabber);

                // Add the frame grabber to the graph
                hr = m_FilterGraph.AddFilter( baseGrabFlt, "Ds.NET Grabber" );
                DsError.ThrowExceptionForHR( hr );

                // ---------------------------------
                // Connect the file filter to the sample grabber

                // Hopefully this will be the video pin, we could check by reading it's mediatype
                IPin iPinOut = DsFindPin.ByDirection(capFilter, PinDirection.Output, 0);

                // Get the input pin from the sample grabber
                IPin iPinIn = DsFindPin.ByDirection(baseGrabFlt, PinDirection.Input, 0);

                hr = m_FilterGraph.Connect(iPinOut, iPinIn);
                DsError.ThrowExceptionForHR( hr );

                // Add the null renderer to the graph
                nullrenderer = new NullRenderer() as IBaseFilter;
                hr = m_FilterGraph.AddFilter( nullrenderer, "Null renderer" );
                DsError.ThrowExceptionForHR( hr );

                // ---------------------------------
                // Connect the sample grabber to the null renderer

                iPinOut = DsFindPin.ByDirection(baseGrabFlt, PinDirection.Output, 0);
                iPinIn = DsFindPin.ByDirection(nullrenderer, PinDirection.Input, 0);
                
                hr = m_FilterGraph.Connect(iPinOut, iPinIn);
                DsError.ThrowExceptionForHR( hr );

                // Turn off the clock.  This causes the frames to be sent
                // thru the graph as fast as possible
                hr = mediaFilt.SetSyncSource(null);
                DsError.ThrowExceptionForHR( hr );

                // Read and cache the image sizes
                SaveSizeInfo(sampGrabber);
            }
            finally
            {
                if (capFilter != null)
                {
                    Marshal.ReleaseComObject(capFilter);
                    capFilter = null;
                }
                if (sampGrabber != null)
                {
                    Marshal.ReleaseComObject(sampGrabber);
                    sampGrabber = null;
                }
                if (nullrenderer != null)
                {
                    Marshal.ReleaseComObject(nullrenderer);
                    nullrenderer = null;
                }
            }
        }

        /// <summary> Read and store the properties </summary>
        private void SaveSizeInfo(ISampleGrabber sampGrabber)
        {
            int hr;

            // Get the media type from the SampleGrabber
            AMMediaType media = new AMMediaType();
            hr = sampGrabber.GetConnectedMediaType( media );
            DsError.ThrowExceptionForHR( hr );

            if( (media.formatType != FormatType.VideoInfo) || (media.formatPtr == IntPtr.Zero) )
            {
                throw new NotSupportedException( "Unknown Grabber Media Format" );
            }

            // Grab the size info
            VideoInfoHeader videoInfoHeader = (VideoInfoHeader) Marshal.PtrToStructure( media.formatPtr, typeof(VideoInfoHeader) );
            m_videoWidth = videoInfoHeader.BmiHeader.Width;
            m_videoHeight = videoInfoHeader.BmiHeader.Height;
            m_stride = m_videoWidth * (videoInfoHeader.BmiHeader.BitCount / 8);

            DsUtils.FreeAMMediaType(media);
            media = null;
        }

        /// <summary> Set the options on the sample grabber </summary>
        private void ConfigureSampleGrabber(ISampleGrabber sampGrabber)
        {
            AMMediaType media;
            int hr;

            // Set the media type to Video/RBG24
            media = new AMMediaType();
            media.majorType	= MediaType.Video;
            media.subType	= MediaSubType.RGB24;
            media.formatType = FormatType.VideoInfo;
            hr = sampGrabber.SetMediaType( media );
            DsError.ThrowExceptionForHR( hr );

            DsUtils.FreeAMMediaType(media);
            media = null;

            // Choose to call BufferCB instead of SampleCB
            hr = sampGrabber.SetCallback( this, 1 );
            DsError.ThrowExceptionForHR( hr );
        }

        /// <summary> Shut down capture </summary>
        private void CloseInterfaces()
        {
            int hr;

            try
            {
                if( m_mediaCtrl != null )
                {
                    // Stop the graph
                    hr = m_mediaCtrl.Stop();
                    m_mediaCtrl = null;
                }
            }
            catch (Exception ex)
            {
                Debug.WriteLine(ex);
            }

#if DEBUG
            if (m_rot != null)
            {
                m_rot.Dispose();
            }
#endif

            if (m_FilterGraph != null)
            {
                Marshal.ReleaseComObject(m_FilterGraph);
                m_FilterGraph = null;
            }
            GC.Collect();
        }

        /// <summary> sample callback, NOT USED. </summary>
        int ISampleGrabberCB.SampleCB( double SampleTime, IMediaSample pSample )
        {
            Marshal.ReleaseComObject(pSample);
            return 0;
        }

        private BasicShotDetectionAlgorithm algo = null;
        public void setShotDetectionAlgorithm(BasicShotDetectionAlgorithm algo)
        {
            this.algo = algo;
        }

        private List<KeyValuePair<int, int>> shots = new List<KeyValuePair<int, int>>();
        private Bitmap previousFrame = null;
        private int start = 0;

        int ISampleGrabberCB.BufferCB( double SampleTime, IntPtr pBuffer, int BufferLen )
        {
            Bitmap currentFrame = IPToBmp(pBuffer); // get new frame

            if (m_Count > 0)
            {
                // Create a stopwatch to measure the time needed for the algorithm
                Stopwatch sw = new Stopwatch();
                sw.Start();
                if (algo.isCut(previousFrame, currentFrame))
                {
                    sw.Stop();
                    Console.WriteLine("Found cut at frame " + m_Count.ToString() + " in {0}ms.", sw.ElapsedMilliseconds);
                    shots.Add(new KeyValuePair<int, int>(start, m_Count));
                    start = m_Count + 1;
                }
                else
                {
                    sw.Stop();
                    Console.WriteLine("Didn't find cut at frame " + m_Count.ToString() + " in {0}ms.", sw.ElapsedMilliseconds);
                }

                previousFrame = null;
            }

            // store current frame
            previousFrame = new Bitmap(currentFrame);

            m_Count++; //counts the number of frames
            return 0;
        }

        public List<KeyValuePair<int, int>> getShots()
        {
            //add the last shot
            int start = shots[shots.Count - 1].Key;
            int stop = m_Count;

            shots.Add(new KeyValuePair<int, int>(start, stop));

            return shots;
        }

        public Bitmap IPToBmp(IntPtr ip)
        {
            // We know the Bits Per Pixel is 24 (3 bytes) because we forced it 
            // to be with sampGrabber.SetMediaType()
            int iBufSize = m_videoWidth * m_videoHeight * 3;

            return new Bitmap(
                m_videoWidth,
                m_videoHeight,
                m_stride,
                PixelFormat.Format24bppRgb,
                ip
            );
        }
    }
}
