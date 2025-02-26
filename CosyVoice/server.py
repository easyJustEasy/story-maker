# -*- coding: utf-8 -*-
import os
import torch
from cosyvoice.cli.cosyvoice import CosyVoice2
from cosyvoice.utils.file_utils import load_wav
import torchaudio
import sys
import uuid
import numpy as np
torch.backends.cudnn.enabled = False
from modelscope import snapshot_download
snapshot_download('iic/CosyVoice2-0.5B', local_dir='pretrained_models/CosyVoice2-0.5B')
from flask import Flask, request, jsonify, Response, stream_with_context, request
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
       model_dir      # 使用半精度浮点数以减少显存占用
)

# 加载示例音频
prompt_speech_16k = load_wav(f'{current_working_directory}/asset/xianyudayin.wav', 16000)

def generate_data(model_output):
    # for i in model_output:
    #     tts_audio = (i['tts_speech'].numpy() * (2 ** 15)).astype(np.int16).tobytes()
    #     yield tts_audio
    for i in model_output:
        tts_audio = (i['tts_speech'].numpy() * (2 ** 15)).astype(np.int16).tobytes()
        yield tts_audio
# 指令语音合成
def run_instruct(text,path="temp"):
  print(f'run_instruct===>{path}')
  for i, j in enumerate(cosyvoice.inference_instruct2(
        text,
        '希望你以后能够做的比我还好呦。',
        prompt_speech_16k,
        stream=True , # 使用流式推理以减少显存占用
        speed=1.1
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
@app.route("/get_voice_remote",methods=["GET", "POST"])
def get_voice_remote():
      # 获取表单数据
    tts_text = request.form.get("tts_text")
    audio = request.form.get("audio")
    # 加载示例音频
    prompt_speech_16k_r = load_wav(f'{current_working_directory}/asset/{audio}.wav', 16000)
    model_output = cosyvoice.inference_instruct2(tts_text,  '希望你以后能够做的比我还好呦。', prompt_speech_16k_r)
    tts_audio = b''
    for i,j in enumerate(model_output):
      tau = (j['tts_speech'].numpy() * (2 ** 15)).astype(np.int16).tobytes()
      tts_audio += tau
    tts_speech = torch.from_numpy(np.array(np.frombuffer(tts_audio, dtype=np.int16))).unsqueeze(dim=0)
    path = os.path.join("temp",f'instruct_{uuid.uuid1()}.wav')
    torchaudio.save(path, tts_speech, cosyvoice.sample_rate)
    def generate():
        with open(path, "rb") as f:
            yield from f
    return Response(generate(), mimetype='application/octet-stream')
if __name__ == '__main__':
    app.run(host="0.0.0.0", port=8000, threaded=True)
