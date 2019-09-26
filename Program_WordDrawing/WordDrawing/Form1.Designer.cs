namespace WordDrawing
{
	partial class Form1
	{
		/// <summary>
		/// Required designer variable.
		/// </summary>
		private System.ComponentModel.IContainer components = null;

		/// <summary>
		/// Clean up any resources being used.
		/// </summary>
		/// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
		protected override void Dispose(bool disposing)
		{
			if (disposing && (components != null))
			{
				components.Dispose();
			}
			base.Dispose(disposing);
		}

		#region Windows Form Designer generated code

		/// <summary>
		/// Required method for Designer support - do not modify
		/// the contents of this method with the code editor.
		/// </summary>
		private void InitializeComponent()
		{
			this.pictureBox1 = new System.Windows.Forms.PictureBox();
			this.toolkit = new System.Windows.Forms.ComboBox();
			this.bringToFront = new System.Windows.Forms.Button();
			this.bringForward = new System.Windows.Forms.Button();
			this.bringToBack = new System.Windows.Forms.Button();
			this.bringBackward = new System.Windows.Forms.Button();
			this.layout = new System.Windows.Forms.Label();
			this.format = new System.Windows.Forms.Label();
			this.label2 = new System.Windows.Forms.Label();
			this.delete = new System.Windows.Forms.Button();
			this.ySelected = new System.Windows.Forms.TextBox();
			this.xSelected = new System.Windows.Forms.TextBox();
			this.label3 = new System.Windows.Forms.Label();
			this.label4 = new System.Windows.Forms.Label();
			this.duplicate = new System.Windows.Forms.Button();
			this.outlineColor = new System.Windows.Forms.Button();
			this.fillColor = new System.Windows.Forms.Button();
			this.colorDialog1 = new System.Windows.Forms.ColorDialog();
			this.colorDialog2 = new System.Windows.Forms.ColorDialog();
			this.noOutline = new System.Windows.Forms.Button();
			this.noFill = new System.Windows.Forms.Button();
			this.fontDialog1 = new System.Windows.Forms.FontDialog();
			this.thicknesskit = new System.Windows.Forms.ComboBox();
			this.thickness = new System.Windows.Forms.Label();
			((System.ComponentModel.ISupportInitialize)(this.pictureBox1)).BeginInit();
			this.SuspendLayout();
			// 
			// pictureBox1
			// 
			this.pictureBox1.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
			this.pictureBox1.Location = new System.Drawing.Point(148, 26);
			this.pictureBox1.Name = "pictureBox1";
			this.pictureBox1.Size = new System.Drawing.Size(753, 410);
			this.pictureBox1.TabIndex = 0;
			this.pictureBox1.TabStop = false;
			this.pictureBox1.Paint += new System.Windows.Forms.PaintEventHandler(this.pictureBox1_Paint);
			this.pictureBox1.MouseDown += new System.Windows.Forms.MouseEventHandler(this.pictureBox1_MouseDown);
			this.pictureBox1.MouseMove += new System.Windows.Forms.MouseEventHandler(this.pictureBox1_MouseMove);
			this.pictureBox1.MouseUp += new System.Windows.Forms.MouseEventHandler(this.pictureBox1_MouseUp);
			this.pictureBox1.Resize += new System.EventHandler(this.pictureBox1_Resize);
			// 
			// toolkit
			// 
			this.toolkit.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
			this.toolkit.FormattingEnabled = true;
			this.toolkit.Items.AddRange(new object[] {
            "Selection Tool",
            "Rectangle",
            "Square",
            "Oval",
            "Circle",
            "Triangle",
            "Pentagon",
            "Hexagon",
            "Octagon",
            "Star"});
			this.toolkit.Location = new System.Drawing.Point(5, 2);
			this.toolkit.Name = "toolkit";
			this.toolkit.Size = new System.Drawing.Size(137, 21);
			this.toolkit.TabIndex = 1;
			// 
			// bringToFront
			// 
			this.bringToFront.Location = new System.Drawing.Point(5, 42);
			this.bringToFront.Name = "bringToFront";
			this.bringToFront.Size = new System.Drawing.Size(137, 32);
			this.bringToFront.TabIndex = 2;
			this.bringToFront.Text = "Bring to front";
			this.bringToFront.UseVisualStyleBackColor = true;
			this.bringToFront.Click += new System.EventHandler(this.bringToFront_Click);
			// 
			// bringForward
			// 
			this.bringForward.Location = new System.Drawing.Point(5, 80);
			this.bringForward.Name = "bringForward";
			this.bringForward.Size = new System.Drawing.Size(137, 30);
			this.bringForward.TabIndex = 4;
			this.bringForward.Text = "Bring forward";
			this.bringForward.UseVisualStyleBackColor = true;
			this.bringForward.Click += new System.EventHandler(this.bringForward_Click);
			// 
			// bringToBack
			// 
			this.bringToBack.Location = new System.Drawing.Point(4, 116);
			this.bringToBack.Name = "bringToBack";
			this.bringToBack.Size = new System.Drawing.Size(138, 31);
			this.bringToBack.TabIndex = 5;
			this.bringToBack.Text = "Bring to back";
			this.bringToBack.UseVisualStyleBackColor = true;
			this.bringToBack.Click += new System.EventHandler(this.bringToBack_Click);
			// 
			// bringBackward
			// 
			this.bringBackward.Location = new System.Drawing.Point(4, 153);
			this.bringBackward.Name = "bringBackward";
			this.bringBackward.Size = new System.Drawing.Size(138, 32);
			this.bringBackward.TabIndex = 6;
			this.bringBackward.Text = "Bring backward";
			this.bringBackward.UseVisualStyleBackColor = true;
			this.bringBackward.Click += new System.EventHandler(this.bringBackward_Click);
			// 
			// layout
			// 
			this.layout.AutoSize = true;
			this.layout.Location = new System.Drawing.Point(58, 26);
			this.layout.Name = "layout";
			this.layout.Size = new System.Drawing.Size(39, 13);
			this.layout.TabIndex = 7;
			this.layout.Text = "Layout";
			// 
			// format
			// 
			this.format.AutoSize = true;
			this.format.Location = new System.Drawing.Point(58, 188);
			this.format.Name = "format";
			this.format.Size = new System.Drawing.Size(39, 13);
			this.format.TabIndex = 8;
			this.format.Text = "Format";
			// 
			// label2
			// 
			this.label2.AutoSize = true;
			this.label2.Location = new System.Drawing.Point(57, 333);
			this.label2.Name = "label2";
			this.label2.Size = new System.Drawing.Size(40, 13);
			this.label2.TabIndex = 9;
			this.label2.Text = "Control";
			// 
			// delete
			// 
			this.delete.Location = new System.Drawing.Point(4, 396);
			this.delete.Name = "delete";
			this.delete.Size = new System.Drawing.Size(138, 41);
			this.delete.TabIndex = 10;
			this.delete.Text = "Delete";
			this.delete.UseVisualStyleBackColor = true;
			this.delete.Click += new System.EventHandler(this.delete_Click);
			// 
			// ySelected
			// 
			this.ySelected.Location = new System.Drawing.Point(299, 2);
			this.ySelected.Name = "ySelected";
			this.ySelected.Size = new System.Drawing.Size(100, 20);
			this.ySelected.TabIndex = 11;
			// 
			// xSelected
			// 
			this.xSelected.Location = new System.Drawing.Point(170, 2);
			this.xSelected.Name = "xSelected";
			this.xSelected.Size = new System.Drawing.Size(100, 20);
			this.xSelected.TabIndex = 12;
			// 
			// label3
			// 
			this.label3.AutoSize = true;
			this.label3.Location = new System.Drawing.Point(276, 5);
			this.label3.Name = "label3";
			this.label3.Size = new System.Drawing.Size(17, 13);
			this.label3.TabIndex = 13;
			this.label3.Text = "Y:";
			// 
			// label4
			// 
			this.label4.AutoSize = true;
			this.label4.Location = new System.Drawing.Point(147, 5);
			this.label4.Name = "label4";
			this.label4.Size = new System.Drawing.Size(17, 13);
			this.label4.TabIndex = 14;
			this.label4.Text = "X:";
			// 
			// duplicate
			// 
			this.duplicate.Location = new System.Drawing.Point(4, 349);
			this.duplicate.Name = "duplicate";
			this.duplicate.Size = new System.Drawing.Size(138, 41);
			this.duplicate.TabIndex = 15;
			this.duplicate.Text = "Duplicate";
			this.duplicate.UseVisualStyleBackColor = true;
			this.duplicate.Click += new System.EventHandler(this.duplicate_Click);
			// 
			// outlineColor
			// 
			this.outlineColor.Location = new System.Drawing.Point(12, 204);
			this.outlineColor.Name = "outlineColor";
			this.outlineColor.Size = new System.Drawing.Size(59, 58);
			this.outlineColor.TabIndex = 16;
			this.outlineColor.Text = "Outline Color";
			this.outlineColor.UseVisualStyleBackColor = true;
			this.outlineColor.Click += new System.EventHandler(this.outlineColor_Click);
			// 
			// fillColor
			// 
			this.fillColor.Location = new System.Drawing.Point(77, 204);
			this.fillColor.Name = "fillColor";
			this.fillColor.Size = new System.Drawing.Size(63, 58);
			this.fillColor.TabIndex = 17;
			this.fillColor.Text = "Fill Color";
			this.fillColor.UseVisualStyleBackColor = true;
			this.fillColor.Click += new System.EventHandler(this.fillColor_Click);
			// 
			// noOutline
			// 
			this.noOutline.Location = new System.Drawing.Point(10, 268);
			this.noOutline.Name = "noOutline";
			this.noOutline.Size = new System.Drawing.Size(60, 35);
			this.noOutline.TabIndex = 18;
			this.noOutline.Text = "No Outline";
			this.noOutline.UseVisualStyleBackColor = true;
			this.noOutline.Click += new System.EventHandler(this.noOutline_Click);
			// 
			// noFill
			// 
			this.noFill.Location = new System.Drawing.Point(76, 268);
			this.noFill.Name = "noFill";
			this.noFill.Size = new System.Drawing.Size(64, 35);
			this.noFill.TabIndex = 19;
			this.noFill.Text = "No Fill";
			this.noFill.UseVisualStyleBackColor = true;
			this.noFill.Click += new System.EventHandler(this.noFill_Click);
			// 
			// thicknesskit
			// 
			this.thicknesskit.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
			this.thicknesskit.FormattingEnabled = true;
			this.thicknesskit.Items.AddRange(new object[] {
            "1",
            "2",
            "4",
            "8",
            "10",
            "14",
            "18"});
			this.thicknesskit.Location = new System.Drawing.Point(103, 309);
			this.thicknesskit.Name = "thicknesskit";
			this.thicknesskit.Size = new System.Drawing.Size(39, 21);
			this.thicknesskit.TabIndex = 20;
			this.thicknesskit.SelectedIndexChanged += new System.EventHandler(this.thicknesskit_SelectedIndexChanged);
			// 
			// thickness
			// 
			this.thickness.AutoSize = true;
			this.thickness.Location = new System.Drawing.Point(2, 312);
			this.thickness.Name = "thickness";
			this.thickness.Size = new System.Drawing.Size(95, 13);
			this.thickness.TabIndex = 21;
			this.thickness.Text = "Outline Thickness:";
			// 
			// Form1
			// 
			this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
			this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
			this.ClientSize = new System.Drawing.Size(903, 438);
			this.Controls.Add(this.thickness);
			this.Controls.Add(this.thicknesskit);
			this.Controls.Add(this.noFill);
			this.Controls.Add(this.noOutline);
			this.Controls.Add(this.fillColor);
			this.Controls.Add(this.outlineColor);
			this.Controls.Add(this.duplicate);
			this.Controls.Add(this.label4);
			this.Controls.Add(this.label3);
			this.Controls.Add(this.xSelected);
			this.Controls.Add(this.ySelected);
			this.Controls.Add(this.delete);
			this.Controls.Add(this.label2);
			this.Controls.Add(this.format);
			this.Controls.Add(this.layout);
			this.Controls.Add(this.bringBackward);
			this.Controls.Add(this.bringToBack);
			this.Controls.Add(this.bringForward);
			this.Controls.Add(this.bringToFront);
			this.Controls.Add(this.toolkit);
			this.Controls.Add(this.pictureBox1);
			this.Name = "Form1";
			this.Text = "Form1";
			((System.ComponentModel.ISupportInitialize)(this.pictureBox1)).EndInit();
			this.ResumeLayout(false);
			this.PerformLayout();

		}

		#endregion

		private System.Windows.Forms.PictureBox pictureBox1;
		private System.Windows.Forms.ComboBox toolkit;
		private System.Windows.Forms.Button bringToFront;
		private System.Windows.Forms.Button bringForward;
		private System.Windows.Forms.Button bringToBack;
		private System.Windows.Forms.Button bringBackward;
		private System.Windows.Forms.Label layout;
		private System.Windows.Forms.Label format;
		private System.Windows.Forms.Label label2;
		private System.Windows.Forms.Button delete;
		private System.Windows.Forms.TextBox ySelected;
		private System.Windows.Forms.TextBox xSelected;
		private System.Windows.Forms.Label label3;
		private System.Windows.Forms.Label label4;
		private System.Windows.Forms.Button duplicate;
		private System.Windows.Forms.Button outlineColor;
		private System.Windows.Forms.Button fillColor;
		private System.Windows.Forms.ColorDialog colorDialog1;
		private System.Windows.Forms.ColorDialog colorDialog2;
		private System.Windows.Forms.Button noOutline;
		private System.Windows.Forms.Button noFill;
		private System.Windows.Forms.FontDialog fontDialog1;
		private System.Windows.Forms.ComboBox thicknesskit;
		private System.Windows.Forms.Label thickness;
	}
}

