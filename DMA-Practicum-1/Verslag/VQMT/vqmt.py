import os
import subprocess

class Tester:
    def __init__(self):
        self.output = 'VQMT Output'
        if not os.path.exists(self.output):
            print('Creating the ' + self.output + ' folder...')
            os.makedirs(self.output)

    def testExe(self):
        ENCODING='iso-8859_15'
        out = "output";
        with open(self.output + '/times.log', 'w', encoding=ENCODING) as log_file:
            for file in os.listdir('output'):
            # Doing it decent would take too long
            original = ''
            name = ''
            sc = ''
            height = '0'
            width = '0'
            if (file.startswith('beowulf')):
                original = 'beowulf_848x352.yuv'
                name = 'beowulf'
                height = '848'
                width = '352'
                frames = '629'
            elif (file.startswith('elephants')):
                original = 'elephants_dream_816x576.yuv'
                name = 'elephant'
                height = '816'
                width = '576'
                frames = '224'
            else:
                original = 'theseekerdarkrising_640x272.yuv'
                name = 'rising'
                height = '640'
                width = '272'
                frames = '630'
            if 'simple' in file:
                sc = 'simple'
            else:
                sc = 'complex'
            print("Starting " + file)
            output = subprocess.Popen(['VQMT.exe', original, 'output/' + file, height, width, frames, self.output + '/' + out , 'PSNR', 'SSIM'], stdout=subprocess.PIPE, stderr=subprocess.STDOUT).communicate()[0]
            f2 = open(self.output + '/' + name + '_times_' + sc + '.csv')
            print(file.split('.')[0] + ': ' + str(output).split(":")[1].split('\\')[0], file = f2)
            f = open(self.output + '/' + name + '_psnr_' + sc + '.csv')
            f1 = open(self.output + '/' + name + '_ssim_' + sc + '.csv')
            print([line.split(',')[1] for line in open(self.output + '/' + out + '_psnr.csv', 'r').readlines() if line.startswith('average')][0], file = f)
            print([line.split(',')[1] for line in open(self.output + '/' + out + '_ssim.csv', 'r').readlines() if line.startswith('average')][0], file = f1)
            os.remove(self.output + '/' + out + '_psnr.csv')
            os.remove(self.output + '/' + out + '_ssim.csv')
            f.close()
            f1.close()
test = Tester()
test.testExe()
