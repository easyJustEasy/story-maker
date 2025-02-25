# -*- coding: utf-8 -*-
import os
import torch
from cosyvoice.cli.cosyvoice import CosyVoice2
from cosyvoice.utils.file_utils import load_wav
import torchaudio
import sys

from flask import Flask, request, jsonify
app = Flask(__name__)
@app.after_request
def after_request(response):
    response.headers.add("Access-Control-Allow-Origin", "*")
    response.headers.add("Access-Control-Allow-Headers", "Content-Type")
    response.headers.add("Access-Control-Allow-Methods", "GET,POST,OPTIONS")
    return response
current_working_directory =os.path.dirname(os.path.abspath(__file__))
# 设置环境变量
sys.path.append(f'{current_working_directory}/third_party/Matcha-TTS')
sys.path.append(f'{current_working_directory}/cosyvoice')
# torch.backends.cudnn.enabled = False
model_dir = f'{current_working_directory}/pretrained_models/CosyVoice2-0.5B'
# 初始化 CosyVoice2 模型
cosyvoice = CosyVoice2(
       model_dir
,
    load_jit=False,  # 如果显存不足，可以尝试设置为 True
    load_trt=False,  # 如果显存不足，可以尝试设置为 True
    fp16=True       # 使用半精度浮点数以减少显存占用
)

# 加载示例音频
prompt_speech_16k = load_wav(f'{current_working_directory}/asset/longyue.wav', 16000)


# 指令语音合成
def run_instruct(text,path):
  print(f'run_instruct===>{path}')
  for i, j in enumerate(cosyvoice.inference_instruct2(
        text,
        '用普通话亲切甜蜜的口吻说这句话',
        prompt_speech_16k,
        stream=True , # 使用流式推理以减少显存占用
        speed=0.8
    )):
   torchaudio.save(os.path.join(path,f'instruct_{i}.wav'), j['tts_speech'], cosyvoice.sample_rate)
   torch.cuda.empty_cache()  # 释放显存

@app.route("/get_voice",methods=["GET", "POST"])
def get_voice():
      # 获取表单数据
    tts_text = request.form.get("tts_text")
    path = request.form.get("path")
    run_instruct(tts_text,path)
    return jsonify({"code":200,"data":{"path":path}})
if __name__ == '__main__':
    app.run(host="0.0.0.0", port=8000, threaded=True)
