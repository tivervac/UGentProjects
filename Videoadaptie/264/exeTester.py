import os
import subprocess

class Tester:
	def __init__(self):
		if not os.path.exists('Encoded'):
			print 'There are no input files!'
			exit()
		if not os.path.exists('Extracted'):
			os.makedirs('Extracted')
		self.input = []
		self.output = []
		
		self.input.append(os.path.join('Encoded', '2.1 A QP30.264'))
		self.input.append(os.path.join('Encoded', '2.1 B QP30 NoIP.264'))
		self.input.append(os.path.join('Encoded', '2.2 A QP30.264'))
		self.input.append(os.path.join('Encoded', '2.2 B QP30 NoIP.264'))
		
		self.output.append(os.path.join('Extracted', '2.1 A.264'))
		self.output.append(os.path.join('Extracted', '2.1 B.264'))
		self.output.append(os.path.join('Extracted', '2.2 A.264'))
		self.output.append(os.path.join('Extracted', '2.2 B.264'))
		self.output.append(os.path.join('Extracted', 'Temp Example.264'))
	
	def testExe(self):
		output = subprocess.Popen(['SVCAdaptie.exe', self.input[0], '-f', '1',self.output[0]], stdout=subprocess.PIPE, stderr=subprocess.STDOUT).communicate()[0]
		output = subprocess.Popen(['SVCAdaptie.exe', self.input[1], '-f', '3',self.output[1]], stdout=subprocess.PIPE, stderr=subprocess.STDOUT).communicate()[0]
		output = subprocess.Popen(['SVCAdaptie.exe', self.input[2], '-l', '1',self.output[2]], stdout=subprocess.PIPE, stderr=subprocess.STDOUT).communicate()[0]
		output = subprocess.Popen(['SVCAdaptie.exe', self.input[3], '-l', '3',self.output[3]], stdout=subprocess.PIPE, stderr=subprocess.STDOUT).communicate()[0]
		'''Example of how to adjust framerate of a file'''
		output = subprocess.Popen(['SVCAdaptie.exe', self.input[0], '-t', '1',self.output[4]], stdout=subprocess.PIPE, stderr=subprocess.STDOUT).communicate()[0]
		
test = Tester()
test.testExe()