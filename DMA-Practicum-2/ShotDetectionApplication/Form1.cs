/****************************************************************************
While the underlying libraries are covered by LGPL, this sample is released 
as public domain.  It is distributed in the hope that it will be useful, but 
WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
or FITNESS FOR A PARTICULAR PURPOSE.  
*****************************************************************************/

using System;
using System.Drawing;
using System.Collections;
using System.ComponentModel;
using System.Windows.Forms;
using System.Runtime.InteropServices;
using System.Data;
using System.Collections.Generic;
using System.Xml;
using System.Diagnostics;
using System.IO;

namespace DxPlay
{
	/// <summary>
	/// Summary description for Form1.
	/// </summary>
	public class Form1 : System.Windows.Forms.Form
    {
        private System.Windows.Forms.Panel panel1;
        private System.Windows.Forms.Button btnStart;
        private System.Windows.Forms.Button btnPause;
        private System.Windows.Forms.Button btnSnap;
        private MenuStrip menuStrip2;
        private ToolStripMenuItem fileToolStripMenuItem;
        private ToolStripMenuItem openVideoToolStripMenuItem;
        private ToolStripSeparator toolStripMenuItem1;
        private ToolStripMenuItem exitToolStripMenuItem;
        private String filename;
        private OpenFileDialog openFileDialog;
        private Button btnStop;
        private SaveFileDialog saveFileDialog;
        private TabControl tabs;
        private TabPage tabPage1;
        private TabPage tabPage2;
        private TabPage tabPage3;
        private TabPage tabPage4;
        private GroupBox groupBox1;
        private Button ME_OK;
        private TextBox ME_threshold;
        private TextBox ME_blocksize;
        private TextBox ME_searchwindow;
        private Label label4;
        private Label label5;
        private Label label6;
        private Button GH_OK;
        private TextBox GH_threshold;
        private TextBox GH_numberofbins;
        private Label label8;
        private Label label9;
        private Button LH_OK;
        private TextBox LH_threshold;
        private TextBox LH_numberofbins;
        private TextBox LH_regionsize;
        private Label label7;
        private Label label10;
        private Label label11;
        private Button PD_OK;
        private TextBox PD_threshold;
        private TextBox PD_secondThreshold;
        private Label label13;
        private Label label14;
        private TabPage tabPage6;
        private Button G_OK;
        private TextBox G_threshold;
        private TextBox G_blocksize;
        private TextBox G_searchwindow;
        private Label G_searchwindow_label;
        private Label label2;
        private Label label3;
        private GroupBox groupBox2;
        private Panel shot_panel;
        private GroupBox groupBox3;
        private Button annotate_button;
        private Label label1;
        private TextBox annotation;
        private ToolStripMenuItem testToolStripMenuItem;
        private ToolStripMenuItem runTestsToolStripMenuItem;
		/// <summary>
		/// Required designer variable.
		/// </summary>
		private System.ComponentModel.Container components = null;

		public Form1()
		{
			//
			// Required for Windows Form Designer support
			//
			InitializeComponent();

			//
			// TODO: Add any constructor code after InitializeComponent call
			//
		}

		/// <summary>
		/// Clean up any resources being used.
		/// </summary>
		protected override void Dispose( bool disposing )
		{
            // Make sure to release the DxPlay object to avoid hanging
            if (m_play != null)
            {
                m_play.Dispose();
            }
			if( disposing )
			{
				if (components != null) 
				{
					components.Dispose();
				}
			}
			base.Dispose( disposing );
		}

		#region Windows Form Designer generated code
		/// <summary>
		/// Required method for Designer support - do not modify
		/// the contents of this method with the code editor.
		/// </summary>
		private void InitializeComponent()
		{
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(Form1));
            this.btnStart = new System.Windows.Forms.Button();
            this.panel1 = new System.Windows.Forms.Panel();
            this.btnPause = new System.Windows.Forms.Button();
            this.btnSnap = new System.Windows.Forms.Button();
            this.menuStrip2 = new System.Windows.Forms.MenuStrip();
            this.fileToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.openVideoToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.toolStripMenuItem1 = new System.Windows.Forms.ToolStripSeparator();
            this.exitToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.openFileDialog = new System.Windows.Forms.OpenFileDialog();
            this.btnStop = new System.Windows.Forms.Button();
            this.saveFileDialog = new System.Windows.Forms.SaveFileDialog();
            this.tabs = new System.Windows.Forms.TabControl();
            this.tabPage1 = new System.Windows.Forms.TabPage();
            this.PD_OK = new System.Windows.Forms.Button();
            this.PD_threshold = new System.Windows.Forms.TextBox();
            this.PD_secondThreshold = new System.Windows.Forms.TextBox();
            this.label13 = new System.Windows.Forms.Label();
            this.label14 = new System.Windows.Forms.Label();
            this.tabPage2 = new System.Windows.Forms.TabPage();
            this.ME_OK = new System.Windows.Forms.Button();
            this.ME_threshold = new System.Windows.Forms.TextBox();
            this.ME_blocksize = new System.Windows.Forms.TextBox();
            this.ME_searchwindow = new System.Windows.Forms.TextBox();
            this.label4 = new System.Windows.Forms.Label();
            this.label5 = new System.Windows.Forms.Label();
            this.label6 = new System.Windows.Forms.Label();
            this.tabPage3 = new System.Windows.Forms.TabPage();
            this.GH_OK = new System.Windows.Forms.Button();
            this.GH_threshold = new System.Windows.Forms.TextBox();
            this.GH_numberofbins = new System.Windows.Forms.TextBox();
            this.label8 = new System.Windows.Forms.Label();
            this.label9 = new System.Windows.Forms.Label();
            this.tabPage4 = new System.Windows.Forms.TabPage();
            this.LH_OK = new System.Windows.Forms.Button();
            this.LH_threshold = new System.Windows.Forms.TextBox();
            this.LH_numberofbins = new System.Windows.Forms.TextBox();
            this.LH_regionsize = new System.Windows.Forms.TextBox();
            this.label7 = new System.Windows.Forms.Label();
            this.label10 = new System.Windows.Forms.Label();
            this.label11 = new System.Windows.Forms.Label();
            this.tabPage6 = new System.Windows.Forms.TabPage();
            this.G_OK = new System.Windows.Forms.Button();
            this.G_threshold = new System.Windows.Forms.TextBox();
            this.G_blocksize = new System.Windows.Forms.TextBox();
            this.G_searchwindow = new System.Windows.Forms.TextBox();
            this.G_searchwindow_label = new System.Windows.Forms.Label();
            this.label2 = new System.Windows.Forms.Label();
            this.label3 = new System.Windows.Forms.Label();
            this.groupBox1 = new System.Windows.Forms.GroupBox();
            this.groupBox2 = new System.Windows.Forms.GroupBox();
            this.shot_panel = new System.Windows.Forms.Panel();
            this.groupBox3 = new System.Windows.Forms.GroupBox();
            this.annotate_button = new System.Windows.Forms.Button();
            this.label1 = new System.Windows.Forms.Label();
            this.annotation = new System.Windows.Forms.TextBox();
            this.testToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.runTestsToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.menuStrip2.SuspendLayout();
            this.tabs.SuspendLayout();
            this.tabPage1.SuspendLayout();
            this.tabPage2.SuspendLayout();
            this.tabPage3.SuspendLayout();
            this.tabPage4.SuspendLayout();
            this.tabPage6.SuspendLayout();
            this.groupBox1.SuspendLayout();
            this.groupBox2.SuspendLayout();
            this.groupBox3.SuspendLayout();
            this.SuspendLayout();
            // 
            // btnStart
            // 
            this.btnStart.Enabled = false;
            this.btnStart.Image = ((System.Drawing.Image)(resources.GetObject("btnStart.Image")));
            this.btnStart.Location = new System.Drawing.Point(9, 223);
            this.btnStart.Name = "btnStart";
            this.btnStart.Size = new System.Drawing.Size(40, 40);
            this.btnStart.TabIndex = 1;
            this.btnStart.Click += new System.EventHandler(this.btnStart_Click);
            // 
            // panel1
            // 
            this.panel1.Location = new System.Drawing.Point(9, 24);
            this.panel1.Name = "panel1";
            this.panel1.Size = new System.Drawing.Size(352, 176);
            this.panel1.TabIndex = 10;
            // 
            // btnPause
            // 
            this.btnPause.Enabled = false;
            this.btnPause.Image = ((System.Drawing.Image)(resources.GetObject("btnPause.Image")));
            this.btnPause.Location = new System.Drawing.Point(55, 223);
            this.btnPause.Name = "btnPause";
            this.btnPause.Size = new System.Drawing.Size(40, 40);
            this.btnPause.TabIndex = 11;
            this.btnPause.Click += new System.EventHandler(this.btnPause_Click);
            // 
            // btnSnap
            // 
            this.btnSnap.Enabled = false;
            this.btnSnap.Location = new System.Drawing.Point(162, 208);
            this.btnSnap.Name = "btnSnap";
            this.btnSnap.Size = new System.Drawing.Size(97, 32);
            this.btnSnap.TabIndex = 12;
            this.btnSnap.Text = "Take snapshot";
            this.btnSnap.Click += new System.EventHandler(this.btnSnap_Click);
            // 
            // menuStrip2
            // 
            this.menuStrip2.Items.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.fileToolStripMenuItem,
            this.testToolStripMenuItem});
            this.menuStrip2.Location = new System.Drawing.Point(0, 0);
            this.menuStrip2.Name = "menuStrip2";
            this.menuStrip2.Size = new System.Drawing.Size(705, 24);
            this.menuStrip2.TabIndex = 14;
            this.menuStrip2.Text = "menuStrip2";
            // 
            // fileToolStripMenuItem
            // 
            this.fileToolStripMenuItem.DropDownItems.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.openVideoToolStripMenuItem,
            this.toolStripMenuItem1,
            this.exitToolStripMenuItem});
            this.fileToolStripMenuItem.Name = "fileToolStripMenuItem";
            this.fileToolStripMenuItem.Size = new System.Drawing.Size(37, 20);
            this.fileToolStripMenuItem.Text = "File";
            // 
            // openVideoToolStripMenuItem
            // 
            this.openVideoToolStripMenuItem.Name = "openVideoToolStripMenuItem";
            this.openVideoToolStripMenuItem.Size = new System.Drawing.Size(152, 22);
            this.openVideoToolStripMenuItem.Text = "Open video...";
            this.openVideoToolStripMenuItem.Click += new System.EventHandler(this.openVideoToolStripMenuItem_Click);
            // 
            // toolStripMenuItem1
            // 
            this.toolStripMenuItem1.Name = "toolStripMenuItem1";
            this.toolStripMenuItem1.Size = new System.Drawing.Size(149, 6);
            // 
            // exitToolStripMenuItem
            // 
            this.exitToolStripMenuItem.Name = "exitToolStripMenuItem";
            this.exitToolStripMenuItem.Size = new System.Drawing.Size(152, 22);
            this.exitToolStripMenuItem.Text = "Exit";
            this.exitToolStripMenuItem.Click += new System.EventHandler(this.exitToolStripMenuItem_Click);
            // 
            // openFileDialog
            // 
            this.openFileDialog.FileName = "openFileDialog1";
            // 
            // btnStop
            // 
            this.btnStop.Enabled = false;
            this.btnStop.Image = ((System.Drawing.Image)(resources.GetObject("btnStop.Image")));
            this.btnStop.Location = new System.Drawing.Point(101, 223);
            this.btnStop.Name = "btnStop";
            this.btnStop.Size = new System.Drawing.Size(40, 40);
            this.btnStop.TabIndex = 15;
            this.btnStop.UseVisualStyleBackColor = true;
            this.btnStop.Click += new System.EventHandler(this.btnStop_Click);
            // 
            // tabs
            // 
            this.tabs.Controls.Add(this.tabPage1);
            this.tabs.Controls.Add(this.tabPage2);
            this.tabs.Controls.Add(this.tabPage3);
            this.tabs.Controls.Add(this.tabPage4);
            this.tabs.Controls.Add(this.tabPage6);
            this.tabs.Location = new System.Drawing.Point(6, 19);
            this.tabs.Name = "tabs";
            this.tabs.SelectedIndex = 0;
            this.tabs.ShowToolTips = true;
            this.tabs.Size = new System.Drawing.Size(261, 243);
            this.tabs.TabIndex = 16;
            // 
            // tabPage1
            // 
            this.tabPage1.Controls.Add(this.PD_OK);
            this.tabPage1.Controls.Add(this.PD_threshold);
            this.tabPage1.Controls.Add(this.PD_secondThreshold);
            this.tabPage1.Controls.Add(this.label13);
            this.tabPage1.Controls.Add(this.label14);
            this.tabPage1.Location = new System.Drawing.Point(4, 22);
            this.tabPage1.Name = "tabPage1";
            this.tabPage1.Padding = new System.Windows.Forms.Padding(3);
            this.tabPage1.Size = new System.Drawing.Size(253, 217);
            this.tabPage1.TabIndex = 0;
            this.tabPage1.Text = "Pixel difference";
            this.tabPage1.UseVisualStyleBackColor = true;
            // 
            // PD_OK
            // 
            this.PD_OK.Location = new System.Drawing.Point(167, 183);
            this.PD_OK.Name = "PD_OK";
            this.PD_OK.Size = new System.Drawing.Size(75, 23);
            this.PD_OK.TabIndex = 13;
            this.PD_OK.Text = "OK";
            this.PD_OK.UseVisualStyleBackColor = true;
            this.PD_OK.Click += new System.EventHandler(this.PD_OK_Click);
            // 
            // PD_threshold
            // 
            this.PD_threshold.Location = new System.Drawing.Point(123, 10);
            this.PD_threshold.Name = "PD_threshold";
            this.PD_threshold.Size = new System.Drawing.Size(119, 20);
            this.PD_threshold.TabIndex = 12;
            // 
            // PD_secondThreshold
            // 
            this.PD_secondThreshold.Location = new System.Drawing.Point(123, 36);
            this.PD_secondThreshold.Name = "PD_secondThreshold";
            this.PD_secondThreshold.Size = new System.Drawing.Size(119, 20);
            this.PD_secondThreshold.TabIndex = 11;
            // 
            // label13
            // 
            this.label13.AutoSize = true;
            this.label13.Location = new System.Drawing.Point(10, 39);
            this.label13.Name = "label13";
            this.label13.Size = new System.Drawing.Size(94, 13);
            this.label13.TabIndex = 8;
            this.label13.Text = "Second Threshold";
            // 
            // label14
            // 
            this.label14.AutoSize = true;
            this.label14.Location = new System.Drawing.Point(10, 13);
            this.label14.Name = "label14";
            this.label14.Size = new System.Drawing.Size(54, 13);
            this.label14.TabIndex = 7;
            this.label14.Text = "Threshold";
            // 
            // tabPage2
            // 
            this.tabPage2.Controls.Add(this.ME_OK);
            this.tabPage2.Controls.Add(this.ME_threshold);
            this.tabPage2.Controls.Add(this.ME_blocksize);
            this.tabPage2.Controls.Add(this.ME_searchwindow);
            this.tabPage2.Controls.Add(this.label4);
            this.tabPage2.Controls.Add(this.label5);
            this.tabPage2.Controls.Add(this.label6);
            this.tabPage2.Location = new System.Drawing.Point(4, 22);
            this.tabPage2.Name = "tabPage2";
            this.tabPage2.Padding = new System.Windows.Forms.Padding(3);
            this.tabPage2.Size = new System.Drawing.Size(253, 217);
            this.tabPage2.TabIndex = 1;
            this.tabPage2.Text = "Motion estimation";
            this.tabPage2.UseVisualStyleBackColor = true;
            // 
            // ME_OK
            // 
            this.ME_OK.Location = new System.Drawing.Point(167, 183);
            this.ME_OK.Name = "ME_OK";
            this.ME_OK.Size = new System.Drawing.Size(75, 23);
            this.ME_OK.TabIndex = 13;
            this.ME_OK.Text = "OK";
            this.ME_OK.UseVisualStyleBackColor = true;
            this.ME_OK.Click += new System.EventHandler(this.ME_OK_Click);
            // 
            // ME_threshold
            // 
            this.ME_threshold.Location = new System.Drawing.Point(123, 10);
            this.ME_threshold.Name = "ME_threshold";
            this.ME_threshold.Size = new System.Drawing.Size(119, 20);
            this.ME_threshold.TabIndex = 12;
            // 
            // ME_blocksize
            // 
            this.ME_blocksize.Location = new System.Drawing.Point(123, 36);
            this.ME_blocksize.Name = "ME_blocksize";
            this.ME_blocksize.Size = new System.Drawing.Size(119, 20);
            this.ME_blocksize.TabIndex = 11;
            // 
            // ME_searchwindow
            // 
            this.ME_searchwindow.Location = new System.Drawing.Point(123, 62);
            this.ME_searchwindow.Name = "ME_searchwindow";
            this.ME_searchwindow.Size = new System.Drawing.Size(119, 20);
            this.ME_searchwindow.TabIndex = 10;
            // 
            // label4
            // 
            this.label4.AutoSize = true;
            this.label4.Location = new System.Drawing.Point(10, 69);
            this.label4.Name = "label4";
            this.label4.Size = new System.Drawing.Size(101, 13);
            this.label4.TabIndex = 9;
            this.label4.Text = "Search window size";
            // 
            // label5
            // 
            this.label5.AutoSize = true;
            this.label5.Location = new System.Drawing.Point(10, 39);
            this.label5.Name = "label5";
            this.label5.Size = new System.Drawing.Size(55, 13);
            this.label5.TabIndex = 8;
            this.label5.Text = "Block size";
            // 
            // label6
            // 
            this.label6.AutoSize = true;
            this.label6.Location = new System.Drawing.Point(10, 13);
            this.label6.Name = "label6";
            this.label6.Size = new System.Drawing.Size(54, 13);
            this.label6.TabIndex = 7;
            this.label6.Text = "Threshold";
            // 
            // tabPage3
            // 
            this.tabPage3.Controls.Add(this.GH_OK);
            this.tabPage3.Controls.Add(this.GH_threshold);
            this.tabPage3.Controls.Add(this.GH_numberofbins);
            this.tabPage3.Controls.Add(this.label8);
            this.tabPage3.Controls.Add(this.label9);
            this.tabPage3.Location = new System.Drawing.Point(4, 22);
            this.tabPage3.Name = "tabPage3";
            this.tabPage3.Padding = new System.Windows.Forms.Padding(3);
            this.tabPage3.Size = new System.Drawing.Size(253, 217);
            this.tabPage3.TabIndex = 2;
            this.tabPage3.Text = "Global histogram";
            this.tabPage3.UseVisualStyleBackColor = true;
            // 
            // GH_OK
            // 
            this.GH_OK.Location = new System.Drawing.Point(167, 183);
            this.GH_OK.Name = "GH_OK";
            this.GH_OK.Size = new System.Drawing.Size(75, 23);
            this.GH_OK.TabIndex = 13;
            this.GH_OK.Text = "OK";
            this.GH_OK.UseVisualStyleBackColor = true;
            this.GH_OK.Click += new System.EventHandler(this.GH_OK_Click);
            // 
            // GH_threshold
            // 
            this.GH_threshold.Location = new System.Drawing.Point(123, 10);
            this.GH_threshold.Name = "GH_threshold";
            this.GH_threshold.Size = new System.Drawing.Size(119, 20);
            this.GH_threshold.TabIndex = 12;
            // 
            // GH_numberofbins
            // 
            this.GH_numberofbins.Location = new System.Drawing.Point(123, 36);
            this.GH_numberofbins.Name = "GH_numberofbins";
            this.GH_numberofbins.Size = new System.Drawing.Size(119, 20);
            this.GH_numberofbins.TabIndex = 11;
            // 
            // label8
            // 
            this.label8.AutoSize = true;
            this.label8.Location = new System.Drawing.Point(10, 39);
            this.label8.Name = "label8";
            this.label8.Size = new System.Drawing.Size(78, 13);
            this.label8.TabIndex = 8;
            this.label8.Text = "Number of bins";
            // 
            // label9
            // 
            this.label9.AutoSize = true;
            this.label9.Location = new System.Drawing.Point(10, 13);
            this.label9.Name = "label9";
            this.label9.Size = new System.Drawing.Size(54, 13);
            this.label9.TabIndex = 7;
            this.label9.Text = "Threshold";
            // 
            // tabPage4
            // 
            this.tabPage4.Controls.Add(this.LH_OK);
            this.tabPage4.Controls.Add(this.LH_threshold);
            this.tabPage4.Controls.Add(this.LH_numberofbins);
            this.tabPage4.Controls.Add(this.LH_regionsize);
            this.tabPage4.Controls.Add(this.label7);
            this.tabPage4.Controls.Add(this.label10);
            this.tabPage4.Controls.Add(this.label11);
            this.tabPage4.Location = new System.Drawing.Point(4, 22);
            this.tabPage4.Name = "tabPage4";
            this.tabPage4.Padding = new System.Windows.Forms.Padding(3);
            this.tabPage4.Size = new System.Drawing.Size(253, 217);
            this.tabPage4.TabIndex = 3;
            this.tabPage4.Text = "Local histogram";
            this.tabPage4.UseVisualStyleBackColor = true;
            // 
            // LH_OK
            // 
            this.LH_OK.Location = new System.Drawing.Point(167, 183);
            this.LH_OK.Name = "LH_OK";
            this.LH_OK.Size = new System.Drawing.Size(75, 23);
            this.LH_OK.TabIndex = 13;
            this.LH_OK.Text = "OK";
            this.LH_OK.UseVisualStyleBackColor = true;
            this.LH_OK.Click += new System.EventHandler(this.LH_OK_Click);
            // 
            // LH_threshold
            // 
            this.LH_threshold.Location = new System.Drawing.Point(123, 10);
            this.LH_threshold.Name = "LH_threshold";
            this.LH_threshold.Size = new System.Drawing.Size(119, 20);
            this.LH_threshold.TabIndex = 12;
            // 
            // LH_numberofbins
            // 
            this.LH_numberofbins.Location = new System.Drawing.Point(123, 36);
            this.LH_numberofbins.Name = "LH_numberofbins";
            this.LH_numberofbins.Size = new System.Drawing.Size(119, 20);
            this.LH_numberofbins.TabIndex = 11;
            // 
            // LH_regionsize
            // 
            this.LH_regionsize.Location = new System.Drawing.Point(123, 62);
            this.LH_regionsize.Name = "LH_regionsize";
            this.LH_regionsize.Size = new System.Drawing.Size(119, 20);
            this.LH_regionsize.TabIndex = 10;
            // 
            // label7
            // 
            this.label7.AutoSize = true;
            this.label7.Location = new System.Drawing.Point(10, 69);
            this.label7.Name = "label7";
            this.label7.Size = new System.Drawing.Size(62, 13);
            this.label7.TabIndex = 9;
            this.label7.Text = "Region size";
            // 
            // label10
            // 
            this.label10.AutoSize = true;
            this.label10.Location = new System.Drawing.Point(10, 39);
            this.label10.Name = "label10";
            this.label10.Size = new System.Drawing.Size(78, 13);
            this.label10.TabIndex = 8;
            this.label10.Text = "Number of bins";
            // 
            // label11
            // 
            this.label11.AutoSize = true;
            this.label11.Location = new System.Drawing.Point(10, 13);
            this.label11.Name = "label11";
            this.label11.Size = new System.Drawing.Size(54, 13);
            this.label11.TabIndex = 7;
            this.label11.Text = "Threshold";
            // 
            // tabPage6
            // 
            this.tabPage6.Controls.Add(this.G_OK);
            this.tabPage6.Controls.Add(this.G_threshold);
            this.tabPage6.Controls.Add(this.G_blocksize);
            this.tabPage6.Controls.Add(this.G_searchwindow);
            this.tabPage6.Controls.Add(this.G_searchwindow_label);
            this.tabPage6.Controls.Add(this.label2);
            this.tabPage6.Controls.Add(this.label3);
            this.tabPage6.Location = new System.Drawing.Point(4, 22);
            this.tabPage6.Name = "tabPage6";
            this.tabPage6.Padding = new System.Windows.Forms.Padding(3);
            this.tabPage6.Size = new System.Drawing.Size(253, 217);
            this.tabPage6.TabIndex = 5;
            this.tabPage6.Text = "Generalized";
            this.tabPage6.UseVisualStyleBackColor = true;
            // 
            // G_OK
            // 
            this.G_OK.Location = new System.Drawing.Point(167, 183);
            this.G_OK.Name = "G_OK";
            this.G_OK.Size = new System.Drawing.Size(75, 23);
            this.G_OK.TabIndex = 13;
            this.G_OK.Text = "OK";
            this.G_OK.UseVisualStyleBackColor = true;
            this.G_OK.Click += new System.EventHandler(this.G_OK_Click);
            // 
            // G_threshold
            // 
            this.G_threshold.Location = new System.Drawing.Point(123, 10);
            this.G_threshold.Name = "G_threshold";
            this.G_threshold.Size = new System.Drawing.Size(119, 20);
            this.G_threshold.TabIndex = 12;
            // 
            // G_blocksize
            // 
            this.G_blocksize.Location = new System.Drawing.Point(123, 36);
            this.G_blocksize.Name = "G_blocksize";
            this.G_blocksize.Size = new System.Drawing.Size(119, 20);
            this.G_blocksize.TabIndex = 11;
            // 
            // G_searchwindow
            // 
            this.G_searchwindow.Location = new System.Drawing.Point(123, 62);
            this.G_searchwindow.Name = "G_searchwindow";
            this.G_searchwindow.Size = new System.Drawing.Size(119, 20);
            this.G_searchwindow.TabIndex = 10;
            // 
            // G_searchwindow_label
            // 
            this.G_searchwindow_label.AutoSize = true;
            this.G_searchwindow_label.Location = new System.Drawing.Point(10, 69);
            this.G_searchwindow_label.Name = "G_searchwindow_label";
            this.G_searchwindow_label.Size = new System.Drawing.Size(101, 13);
            this.G_searchwindow_label.TabIndex = 9;
            this.G_searchwindow_label.Text = "Search window size";
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(10, 39);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(78, 13);
            this.label2.TabIndex = 8;
            this.label2.Text = "Number of bins";
            // 
            // label3
            // 
            this.label3.AutoSize = true;
            this.label3.Location = new System.Drawing.Point(10, 13);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(54, 13);
            this.label3.TabIndex = 7;
            this.label3.Text = "Threshold";
            // 
            // groupBox1
            // 
            this.groupBox1.Controls.Add(this.tabs);
            this.groupBox1.Location = new System.Drawing.Point(411, 164);
            this.groupBox1.Name = "groupBox1";
            this.groupBox1.Size = new System.Drawing.Size(276, 273);
            this.groupBox1.TabIndex = 17;
            this.groupBox1.TabStop = false;
            this.groupBox1.Text = "Shot detection";
            // 
            // groupBox2
            // 
            this.groupBox2.Controls.Add(this.shot_panel);
            this.groupBox2.Location = new System.Drawing.Point(12, 28);
            this.groupBox2.Name = "groupBox2";
            this.groupBox2.Size = new System.Drawing.Size(681, 130);
            this.groupBox2.TabIndex = 18;
            this.groupBox2.TabStop = false;
            this.groupBox2.Text = "Shots";
            // 
            // shot_panel
            // 
            this.shot_panel.AutoScroll = true;
            this.shot_panel.Location = new System.Drawing.Point(7, 14);
            this.shot_panel.Name = "shot_panel";
            this.shot_panel.Size = new System.Drawing.Size(668, 103);
            this.shot_panel.TabIndex = 0;
            // 
            // groupBox3
            // 
            this.groupBox3.Controls.Add(this.annotate_button);
            this.groupBox3.Controls.Add(this.label1);
            this.groupBox3.Controls.Add(this.annotation);
            this.groupBox3.Controls.Add(this.panel1);
            this.groupBox3.Controls.Add(this.btnSnap);
            this.groupBox3.Controls.Add(this.btnStart);
            this.groupBox3.Controls.Add(this.btnStop);
            this.groupBox3.Controls.Add(this.btnPause);
            this.groupBox3.Location = new System.Drawing.Point(12, 164);
            this.groupBox3.Name = "groupBox3";
            this.groupBox3.Size = new System.Drawing.Size(385, 272);
            this.groupBox3.TabIndex = 19;
            this.groupBox3.TabStop = false;
            this.groupBox3.Text = "Video";
            // 
            // annotate_button
            // 
            this.annotate_button.Enabled = false;
            this.annotate_button.Location = new System.Drawing.Point(342, 244);
            this.annotate_button.Name = "annotate_button";
            this.annotate_button.Size = new System.Drawing.Size(37, 23);
            this.annotate_button.TabIndex = 18;
            this.annotate_button.Text = "ok";
            this.annotate_button.UseVisualStyleBackColor = true;
            this.annotate_button.Click += new System.EventHandler(this.annotate_button_Click);
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(159, 251);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(76, 13);
            this.label1.TabIndex = 17;
            this.label1.Text = "Annotate shot:";
            // 
            // annotation
            // 
            this.annotation.Location = new System.Drawing.Point(236, 246);
            this.annotation.Name = "annotation";
            this.annotation.Size = new System.Drawing.Size(100, 20);
            this.annotation.TabIndex = 16;
            // 
            // testToolStripMenuItem
            // 
            this.testToolStripMenuItem.DropDownItems.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.runTestsToolStripMenuItem});
            this.testToolStripMenuItem.Name = "testToolStripMenuItem";
            this.testToolStripMenuItem.Size = new System.Drawing.Size(41, 20);
            this.testToolStripMenuItem.Text = "Test";
            // 
            // runTestsToolStripMenuItem
            // 
            this.runTestsToolStripMenuItem.Name = "runTestsToolStripMenuItem";
            this.runTestsToolStripMenuItem.Size = new System.Drawing.Size(152, 22);
            this.runTestsToolStripMenuItem.Text = "Run tests...";
            this.runTestsToolStripMenuItem.Click += new System.EventHandler(this.runTestsToolStripMenuItem_Click);
            // 
            // Form1
            // 
            this.AutoScaleBaseSize = new System.Drawing.Size(5, 13);
            this.ClientSize = new System.Drawing.Size(705, 444);
            this.Controls.Add(this.groupBox2);
            this.Controls.Add(this.menuStrip2);
            this.Controls.Add(this.groupBox1);
            this.Controls.Add(this.groupBox3);
            this.Name = "Form1";
            this.Text = "DxPlay";
            this.FormClosing += new System.Windows.Forms.FormClosingEventHandler(this.closing);
            this.menuStrip2.ResumeLayout(false);
            this.menuStrip2.PerformLayout();
            this.tabs.ResumeLayout(false);
            this.tabPage1.ResumeLayout(false);
            this.tabPage1.PerformLayout();
            this.tabPage2.ResumeLayout(false);
            this.tabPage2.PerformLayout();
            this.tabPage3.ResumeLayout(false);
            this.tabPage3.PerformLayout();
            this.tabPage4.ResumeLayout(false);
            this.tabPage4.PerformLayout();
            this.tabPage6.ResumeLayout(false);
            this.tabPage6.PerformLayout();
            this.groupBox1.ResumeLayout(false);
            this.groupBox2.ResumeLayout(false);
            this.groupBox3.ResumeLayout(false);
            this.groupBox3.PerformLayout();
            this.ResumeLayout(false);
            this.PerformLayout();

        }
		#endregion

		/// <summary>
		/// The main entry point for the application.
		/// </summary>
		[STAThread]
		static void Main() 
		{
			Application.Run(new Form1());
		}


        /****************
         *   Testcode   *
         ****************/
        String path = "E:\\Mijn Documenten\\Dropbox\\Computerwetenschappen 2014 - 2015\\1e semester\\Ontwerp van multimediatoepassingen\\Lab sessions\\Lab 2\\";
        //String path = "C:\\Users\\Gebruiker\\Data\\";
        String[] video = { "return_jedi_trailer_cuts-only.avi", "youth_without_youth.avi", "csi.avi", };
        String[] GT_files = { "return_jedi_trailer_cuts-only_GT.xml", "youth_without_youth_GT.xml", "csi_GT.xml", };

        private void runTests(){
            /*tester.DoWork += new DoWorkEventHandler(tester_DoWork);
            backgroundWorker1.RunWorkerAsync();*/

            for (int i = 0; i < video.Length; i++)
            {
                //open video
                filename = path + video[i];
                m_play = null;
                m_State = State.Changed;
                m_play = new DxPlay(panel1, filename);
                

                //get ground thruth
                List<KeyValuePair<int, int>> GT_shots = get_GT_shots(path + GT_files[i]);

                //call testfunctions here
                test_pixel(i, GT_shots);
                
            }
        }

        private void test_pixel(int i, List<KeyValuePair<int, int>> GT_shots)
        {
            Stopwatch sw = new Stopwatch();
            //Pixel Difference
            //output
            StreamWriter outputfile_LH = File.AppendText(path + "output\\" + "pix2_" + i + ".dat");
            outputfile_LH.WriteLine("#" + video[i]);
            outputfile_LH.WriteLine("# j \t k \t precision \t recall \t time");
            outputfile_LH.Flush();
            for (int j = 100; j <= 200; j += 20)
            {
                for (int k = 30; k <= 100; k += 10)
                {
                    sw.Reset();

                    //run algorithm
                    BasicShotDetectionAlgorithm algo = new PixelDifferenceSD(j, k);
                    sw.Start();
                    List<KeyValuePair<int, int>> shots = m_play.DetectShots(algo);
                    sw.Stop();

                    //calculate precision and recall values
                    double recall = 0;
                    double precision = 0;
                    long time = sw.ElapsedMilliseconds;
                    calculate_precision_and_recall(shots, GT_shots, out precision, out recall);

                    //output
                    outputfile_LH.WriteLine(j + "\t" + k + "\t" + precision + "\t" + recall + "\t" + time);
                    outputfile_LH.Flush();
                    Console.WriteLine("region size: " + j);
                    Console.WriteLine("precision: " + precision);
                    Console.WriteLine("recall: " + recall);
                    Console.WriteLine("time: " + time);
                }
            }
            outputfile_LH.Close();
        }

        private void test_LH(int i, List<KeyValuePair<int, int>> GT_shots)
        {
            int width = m_play.getWidth();
            int height = m_play.getHeight();
            Stopwatch sw = new Stopwatch();
            //Local Histogram
            //output
            //System.IO.StreamWriter outputfile_LH = new System.IO.StreamWriter(path + "output\\" + "LH_" + i + ".dat");
            StreamWriter outputfile_LH = File.AppendText(path + "output\\" + "LH_" + i + ".dat");
            outputfile_LH.WriteLine("#" + video[i]);
            outputfile_LH.WriteLine("# j \t k \t precision \t recall \t time");
            outputfile_LH.Flush();
            for (int j = 8; j <= Math.Min(width / 9, height / 9); j += 4)
            {
                if (i == 2)
                {//csi
                    if (j == 8)
                        j = j * 2;
                    else
                        j += 4;
                }
                for (int k = 256; k <= 4; k++)
                {
                    sw.Reset();

                    //run algorithm
                    BasicShotDetectionAlgorithm algo = new LocalHistogramSD(30, k, j);
                    sw.Start();
                    List<KeyValuePair<int, int>> shots = m_play.DetectShots(algo);
                    sw.Stop();

                    //calculate precision and recall values
                    double recall = 0;
                    double precision = 0;
                    long time = sw.ElapsedMilliseconds;
                    calculate_precision_and_recall(shots, GT_shots, out precision, out recall);

                    //output
                    outputfile_LH.WriteLine(j + "\t" + k + "\t" + precision + "\t" + recall + "\t" + time);
                    outputfile_LH.Flush();
                    Console.WriteLine("region size: " + j);
                    Console.WriteLine("precision: " + precision);
                    Console.WriteLine("recall: " + recall);
                    Console.WriteLine("time: " + time);
                }
            }
            outputfile_LH.Close();
        }

        private void test_GH(int i, List<KeyValuePair<int, int>> GT_shots)
        {
            int width = m_play.getWidth();
            int height = m_play.getHeight();
            Stopwatch sw = new Stopwatch();
            //Local Histogram
            //output
            //System.IO.StreamWriter outputfile_LH = new System.IO.StreamWriter(path + "output\\" + "LH_" + i + ".dat");
            StreamWriter outputfile_LH = File.AppendText(path + "output\\" + "LH_" + i + ".dat");
            outputfile_LH.WriteLine("#" + video[i]);
            outputfile_LH.WriteLine("# j \t k \t precision \t recall \t time");
            outputfile_LH.Flush();
            for (int j = 8; j <= Math.Min(width / 9, height / 9); j += 4)
            {
                if (i == 2)
                {//csi
                    if (j == 8)
                        j = j * 2;
                    else
                        j += 4;
                }
                for (int k = 256; k >= 4; k = k / 2)
                {
                    sw.Reset();

                    //run algorithm
                    BasicShotDetectionAlgorithm algo = new LocalHistogramSD(30, k, j);
                    sw.Start();
                    List<KeyValuePair<int, int>> shots = m_play.DetectShots(algo);
                    sw.Stop();

                    //calculate precision and recall values
                    double recall = 0;
                    double precision = 0;
                    long time = sw.ElapsedMilliseconds;
                    calculate_precision_and_recall(shots, GT_shots, out precision, out recall);

                    //output
                    outputfile_LH.WriteLine(j + "\t" + k + "\t" + precision + "\t" + recall + "\t" + time);
                    outputfile_LH.Flush();
                    Console.WriteLine("region size: " + j);
                    Console.WriteLine("precision: " + precision);
                    Console.WriteLine("recall: " + recall);
                    Console.WriteLine("time: " + time);
                }
            }
            outputfile_LH.Close();
        }

        private List<KeyValuePair<int, int>> get_GT_shots(String GT_file){
            List<KeyValuePair<int, int>> GT_shots = new List<KeyValuePair<int, int>>();
            XmlDocument doc = new XmlDocument();
            doc.Load(GT_file);
            XmlNode shots = doc.SelectSingleNode("ShotDetection");
            foreach (XmlNode shot in shots){
                String text = shot.InnerText;
                string[] frame_nrs = text.Split('-');
                GT_shots.Add(new KeyValuePair<int,int>(Int32.Parse(frame_nrs[0]), Int32.Parse(frame_nrs[1])));
            }

            return GT_shots;
        }

        private void calculate_precision_and_recall(List<KeyValuePair<int, int>> shots, List<KeyValuePair<int, int>> GT_shots, out double precision, out double recall)
        {
            double true_positive = 0;
            double false_positive = 0;
            double false_negative = 0;

            int i=0;//position of GT
            int j=0;//position shots
            while(i != GT_shots.Count && j!= shots.Count)
            {
                KeyValuePair<int, int> gt = GT_shots[i];
                KeyValuePair<int, int> shot = shots[j];
                if (shot.Value == gt.Value) {
                    true_positive += 1;
                    i++;
                    j++;
                }
                else if (shot.Value < gt.Value)
                {
                    false_positive += 1;
                    j++;
                }
                else
                {
                    false_negative += 1;
                    i++;
                }
            }

            recall = true_positive / (true_positive + false_negative);
            precision = true_positive / (true_positive + false_positive);
        }
        /************************************************/

        enum State
        {
            Uninit,
            Changed,
            Stopped,
            Paused,
            Playing
        }
        State m_State = State.Uninit;
        DxPlay m_play = null;

        private void btnStart_Click(object sender, System.EventArgs e)
        {
            // If necessary, close the old file
            if (m_State == State.Stopped)
            {
                // Did the filename change?
                if (filename != null && filename != m_play.FileName)
                {
                    // File name changed, close the old file
                    m_play.Dispose();
                    m_play = null;
                    m_State = State.Uninit;
                    btnSnap.Enabled = false;
                }
            }

            // If we have no file open
            if (m_play == null)
            {
                try
                {
                    // Open the file, provide a handle to play it in
                    m_play = new DxPlay(panel1, filename);
                    /*int width = m_play.getWidth();
                    int height = m_play.getHeight();

                    panel1.Size = new Size(width, height);*/
                    // Let us know when the file is finished playing
                    m_play.StopPlay += new DxPlay.DxPlayEvent(m_play_StopPlay);
                    m_State = State.Stopped;
                }
                catch(COMException ce)
                {
                    MessageBox.Show("Failed to open file: " + ce.Message, "Open Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                }
            }

            // If we were stopped or paused, start
            if (m_State == State.Stopped || m_State == State.Paused)
            {
                m_play.Start();
                btnSnap.Enabled = true;
                btnPause.Enabled = true;
                btnStop.Enabled = true;
                btnStart.Enabled = false;
                m_State = State.Playing;
            }
        }

        private void btnStop_Click(object sender, EventArgs e)
        {
            // If we are playing or paused, stop
            if (m_State == State.Playing || m_State == State.Paused)
            {
                m_play.Stop();
                m_play.Rewind();
                btnPause.Enabled = false;
                btnStart.Enabled = true;
                btnStop.Enabled = false;
                m_State = State.Stopped;
            }
        }

        private void btnPause_Click(object sender, System.EventArgs e)
        {
            // If we are playing, pause
            if (m_State == State.Playing)
            {
                m_play.Pause();
                btnStart.Enabled = true;
                btnPause.Enabled = false;
                m_State = State.Paused;
            }
        }

        private void btnSnap_Click(object sender, System.EventArgs e)
        {
            // Grab a copy of the current bitmap.  Graph can be paused, playing, or stopped
            IntPtr ip = m_play.SnapShot();
            try
            {
                // Turn the raw pixels into a Bitmap
                Bitmap bmp = m_play.IPToBmp(ip);

                saveFileDialog.FileName = "Snapshot.bmp";
                saveFileDialog.AddExtension = true;
                saveFileDialog.Filter = "Bitmap (*.bmp)|*.bmp";
                DialogResult result = saveFileDialog.ShowDialog();
                if (result != DialogResult.Cancel)
                {
                    String filename = saveFileDialog.FileName;
                    // Save the bitmap to a file
                    bmp.Save(@filename);
                }
            }
            finally
            {
                // Free the raw pixels
                Marshal.FreeCoTaskMem(ip);
            }
        }

        // Called when the video is finished playing
        private void m_play_StopPlay(Object sender)
        {
            // This isn't the right way to do this, but heck, it's only a sample
            CheckForIllegalCrossThreadCalls = false;
            btnPause.Enabled = false;
            btnStart.Enabled = true;
            btnStop.Enabled = false;
            

            CheckForIllegalCrossThreadCalls = true;

            m_State = State.Stopped;

            // Rewind clip to beginning to allow DxPlay.Start to work again.
            m_play.Rewind();
        }

        private void openVideoToolStripMenuItem_Click(object sender, EventArgs e)
        {
            openFileDialog.Title = "Choose a video...";
            openFileDialog.Filter = "avi files (*.avi)|*.avi";
            DialogResult result = openFileDialog.ShowDialog();

            if (result != DialogResult.Cancel)
            {
                filename = openFileDialog.FileName;
                btnStart.Enabled = true;
                btnPause.Enabled = false;
                btnStop.Enabled = false;
                m_play = null;
                m_State = State.Changed;
            }
            btnStart_Click(null, null);
        }

        private void exitToolStripMenuItem_Click(object sender, EventArgs e)
        {
            Environment.Exit(0);
        }

        private void fillShotPanel(List<KeyValuePair<int, int>> frame_nrs)
        {
            shot_panel.Controls.Clear();
            Console.WriteLine("Size: " + frame_nrs.Count);
            for (int i = 0; i < frame_nrs.Count; i++)
            {
                int begin = frame_nrs[i].Key;
                int end = frame_nrs[i].Value;
                Bitmap bmp = m_play.getShotOfFrame(frame_nrs[i].Key);
                //resize
                int resized_width = bmp.Width * 68 / bmp.Height;
                Bitmap resized = new Bitmap(bmp, new Size(resized_width, 68));
                //covert to picturebox
                PictureBox picture = new PictureBox();
                picture.Image = resized;
                picture.Location = new Point((resized_width * i), 4);
                //picture.BorderStyle = BorderStyle.FixedSingle;
                //label
                Label label = new Label();
                label.Text = frame_nrs[i].Key + "-" + frame_nrs[i].Value;
                label.Location = new Point((resized_width * i), 68);
                //add to panel
                shot_panel.Controls.Add(picture);
                shot_panel.Controls.Add(label);

                //add click listener
                picture.Click += new EventHandler((sender1, e) => playShot(sender1, null, begin, end));

            }

        }


        private void playShot(object sender, EventArgs e, int beginFrame, int endFrame)
        {
            m_play.playShot(beginFrame,endFrame);
            current_shot_begin = beginFrame;
            current_shot_end = endFrame;
            annotate_button.Enabled = true;
        }

        /****************/
        /*Shot detection*/
        /****************/
        


        private void PD_OK_Click(object sender, EventArgs e)
        {
            if (m_play != null)
            {
                //get parameters
                double threshold = Double.Parse(PD_threshold.Text);
                double secondTreshold = Double.Parse(PD_secondThreshold.Text);

                //call shot detection algo Pixel Difference
                BasicShotDetectionAlgorithm algorithm = new PixelDifferenceSD(threshold, secondTreshold);

                executeAlgortihm(algorithm);
            }
        }

        private void ME_OK_Click(object sender, EventArgs e)
        {
            if (m_play != null)
            {
                //get parameters
                double threshold = Double.Parse(ME_threshold.Text);
                int blocksize = Int32.Parse(ME_blocksize.Text);
                int search_window_size = Int32.Parse(ME_searchwindow.Text);

                //call shot detection algorithm Motion Estimation
                BasicShotDetectionAlgorithm algorithm = new MotionEstimationSD(threshold, blocksize, search_window_size);
                executeAlgortihm(algorithm);
            }
        }

        private void GH_OK_Click(object sender, EventArgs e)
        {
            if (m_play != null)
            {
                //get parameters
                double threshold = Double.Parse(GH_threshold.Text);
                int number_of_bins = Int32.Parse(GH_numberofbins.Text);

                //call shot detection algorithm Global Histogram
                BasicShotDetectionAlgorithm algorithm = new GlobalHistogramSD(threshold, number_of_bins);
                executeAlgortihm(algorithm);
            }
        }

        private void LH_OK_Click(object sender, EventArgs e)
        {
            if (m_play != null)
            {
                //get parameters
                double threshold = Double.Parse(LH_threshold.Text);
                int number_of_bins = Int32.Parse(LH_numberofbins.Text);
                int region_size = Int32.Parse(LH_regionsize.Text);

                //call shot detection algorithm Local Histogram
                BasicShotDetectionAlgorithm algorithm = new LocalHistogramSD(threshold, number_of_bins, region_size);
                executeAlgortihm(algorithm);
            }
        }

        private void G_OK_Click(object sender, EventArgs e)
        {
            if (m_play != null)
            {
                //get parameters
                double threshold = Double.Parse(G_threshold.Text);
                //int blocksize = Int32.Parse(G_blocksize.Text);
                int search_window_size = Int32.Parse(G_searchwindow.Text);

                //call shot detection algorithm Generalized
                BasicShotDetectionAlgorithm algorithm = new SSIMSD(threshold, search_window_size);
                executeAlgortihm(algorithm);
            }
        }

        private void closing(object sender, FormClosingEventArgs e)
        {
            Environment.Exit(0);
        }

        private void executeAlgortihm(BasicShotDetectionAlgorithm algo)
        {          
            backgroundWorker1.DoWork += new DoWorkEventHandler((sender1, e) => backgroundWorker1_DoWork(sender1, null, algo));
            backgroundWorker1.RunWorkerCompleted += new RunWorkerCompletedEventHandler(backgroundWorker1_RunWorkerCompleted);
            backgroundWorker1.RunWorkerAsync();

            MessageBox.Show("Processing... Please wait", "Processing...", MessageBoxButtons.OK, MessageBoxIcon.Warning);

            shot_panel.Controls.Clear();
            Label label = new Label();
            label.Location = new Point(300, 50);
            label.Text = "Processing...";
            //add to panel
            shot_panel.Controls.Add(label);
        }

        //backgroundworker
        private System.ComponentModel.BackgroundWorker backgroundWorker1 = new BackgroundWorker();
        private List<KeyValuePair<int, int>> start_stop_s;
        private void backgroundWorker1_DoWork(object sender, DoWorkEventArgs e, BasicShotDetectionAlgorithm algo)
        {
            start_stop_s = m_play.DetectShots(algo);
            Console.WriteLine("background process completed");
        }

        private void backgroundWorker1_RunWorkerCompleted(object sender, RunWorkerCompletedEventArgs e)
        {
            fillShotPanel(start_stop_s);
            MessageBox.Show("Ready!", "Ready!", MessageBoxButtons.OK, MessageBoxIcon.Warning);
        }

        private int current_shot_begin;
        private int current_shot_end;

        private void annotate_button_Click(object sender, EventArgs e)
        {
            String info = annotation.Text;
            m_play.annotateShot(current_shot_begin, current_shot_end, info);
            MessageBox.Show("Annotation saved!", "Ready", MessageBoxButtons.OK, MessageBoxIcon.None);
            annotation.Clear();
        }

        private void runTestsToolStripMenuItem_Click(object sender, EventArgs e)
        {
            runTests();
        }
    }
}
