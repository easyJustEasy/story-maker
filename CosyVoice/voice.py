import os
import torch
from cosyvoice.cli.cosyvoice import CosyVoice2
from cosyvoice.utils.file_utils import load_wav
import torchaudio
import sys
# 设置环境变量
sys.path.append('third_party/Matcha-TTS')
sys.path.append("cosyvoice")
torch.backends.cudnn.enabled = False
model_dir = "pretrained_models/CosyVoice2-0.5B"
# 初始化 CosyVoice2 模型
cosyvoice = CosyVoice2(
       model_dir
,
    load_jit=False,  # 如果显存不足，可以尝试设置为 True
    load_trt=False,  # 如果显存不足，可以尝试设置为 True
    fp16=True       # 使用半精度浮点数以减少显存占用
)

# 加载示例音频
prompt_speech_16k = load_wav('./asset/longyue.wav', 16000)


# 指令语音合成
def run_instruct(text):
  for i, j in enumerate(cosyvoice.inference_instruct2(
        text,
        '用普通话亲切甜蜜的口吻说这句话',
        prompt_speech_16k,
        stream=True  # 使用流式推理以减少显存占用
    )):
        torchaudio.save(f'temp/uuuu_instruct_{i}.wav', j['tts_speech'], cosyvoice.sample_rate)
        torch.cuda.empty_cache()  # 释放显存

# 运行推理
if __name__ == "__main__":
    text ='''
    第十一集 自动晾衣架

    在一个阳光明媚的早晨，飞飞醒来后发现妈妈已经在忙碌地晾晒衣物。妈妈的脸上带着一丝疲惫，显然她已经连续几天都在为家里的衣物操劳。看着妈妈忙碌的身影，飞飞心里涌起一股温暖的情感，同时也萌生了一个想法：他要发明一个能自动晾衣服的装置，让妈妈不再那么辛苦。

    飞飞走进自己的小实验室，那里摆满了各种各样的工具和材料。他首先在笔记本上画出了一个简单的草图，设计了一个可以自动伸缩和旋转的晾衣架。这个晾衣架需要能够根据天气变化自动调整位置，以便更好地晾干衣物。飞飞还设想了一个传感器系统，可以检测衣物是否已经完全晾干，从而自动收回晾衣架。

    接下来，飞飞开始动手制作。他首先用废旧的自行车链条和齿轮制作了一个可以自动伸缩的机械臂。然后，他找来了几个废旧的风扇叶片，将它们固定在机械臂的末端，作为晾衣架的支撑结构。为了使晾衣架能够自动旋转，飞飞还安装了一个小型电机和一个定时器。这样，晾衣架就可以按照预定的时间间隔自动旋转，确保每件衣物都能均匀晾干。

    在制作过程中，飞飞遇到了不少困难。例如，如何让机械臂既坚固又灵活，如何确保电机和传感器的稳定运行。但他没有放弃，而是不断地尝试和改进。每当遇到问题时，他就会查阅相关的书籍或上网搜索资料，寻求解决方案。他还向邻居的叔叔请教了一些机械方面的知识，这让他受益匪浅。

    经过几天的努力，飞飞终于完成了他的自动晾衣架。他把它安装在阳台的角落里，然后邀请妈妈一起来测试。当妈妈把衣物挂在晾衣架上时，一切都按计划顺利进行。晾衣架自动伸缩，将衣物均匀地晾开。随着定时器的启动，晾衣架开始缓慢旋转，确保每件衣物都能得到充分的阳光照射。不久之后，衣物便被彻底晾干，晾衣架自动收回，衣物整齐地折叠在一起。

    妈妈看着这一切，感动得几乎落泪。她紧紧抱住飞飞，感激地说：“儿子，你真是太棒了！这个发明真的帮了我大忙。” 飞飞笑着回答：“这是我应该做的，妈妈。我希望这个发明能让您少一些劳累，多一些时间休息。”

    这次经历让飞飞深刻体会到了科技的力量。他意识到，通过自己的努力和创造力，可以解决日常生活中的许多问题。更重要的是，他学会了如何与家人共同分享快乐和成就感。飞飞知道，未来还有更多的挑战等待着他，但他已经准备好迎接每一个新的冒险。

    从那以后，飞飞的自动晾衣架成了邻居们羡慕的对象。他们纷纷前来参观，并询问飞飞是如何做到的。飞飞总是乐于分享自己的经验和心得，鼓励大家勇于尝试和创新。他相信，只要每个人都愿意付出努力，就能创造出更多美好的事物，让生活变得更加美好。

    通过这次发明，飞飞不仅减轻了妈妈的负担，还学到了许多宝贵的生活经验。他明白了团队合作的重要性，学会了如何克服困难，更重要的是，他感受到了家庭的温暖和支持。飞飞知道，无论未来遇到什么挑战，他都不会孤单，因为有爱他的家人和朋友陪伴在他身边。

    '''
    run_instruct(text)