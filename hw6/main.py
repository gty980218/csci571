# Copyright 2018 Google LLC
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

# [START gae_python38_app]
# [START gae_python3_app]
from flask import Flask
from flask import render_template
from flask import request, jsonify
import finnhub
from datetime import datetime
from dateutil.relativedelta import relativedelta, MO

# If `entrypoint` is not defined in app.yaml, App Engine will look for an app
# called `app` in `main.py`.
app = Flask(__name__)
TOCKEN = "c7t28riad3i9jn7rol5g"


@app.route('/')
def main():
    return app.send_static_file('index.html')


@app.route("/get_company/<string:symbol>", methods=['GET'])
def getCompany(symbol):
    finnhub_client = finnhub.Client(api_key=TOCKEN)
    json = finnhub_client.company_profile2(symbol=symbol)
    return jsonify(json)


@app.route("/get_summary/<string:symbol>", methods=['GET'])
def getSummary(symbol):
    finnhub_client = finnhub.Client(api_key=TOCKEN)
    json = finnhub_client.quote(symbol=symbol)
    return jsonify(json)


@app.route("/get_rec/<string:symbol>", methods=['GET'])
def getRec(symbol):
    finnhub_client = finnhub.Client(api_key=TOCKEN)
    json = finnhub_client.recommendation_trends(symbol=symbol)
    return jsonify(json)


@app.route("/get_charts/<string:symbol>", methods=['GET'])
def getCharts(symbol):
    now = datetime.now()
    pre = now + relativedelta(months=-6)
    finnhub_client = finnhub.Client(api_key=TOCKEN)
    json = finnhub_client.stock_candles(symbol, 'D', int(datetime.timestamp(pre)), int(datetime.timestamp(now)))
    print(int(datetime.timestamp(pre)))
    return jsonify(json)


@app.route("/get_news/<string:symbol>", methods=['GET'])
def getNews(symbol):
    now = datetime.now().date()
    pre = now + relativedelta(months=-1)
    finnhub_client = finnhub.Client(api_key=TOCKEN)
    json = finnhub_client.company_news(symbol=symbol, _from=pre, to=now)
    return jsonify(json)


if __name__ == '__main__':
    # This is used when running locally only. When deploying to Google App
    # Engine, a webserver process such as Gunicorn will serve the app. You
    # can configure startup instructions by adding `entrypoint` to app.yaml.
    app.run(host='127.0.0.1', port=8080, debug=True)
# [END gae_python3_app]
# [END gae_python38_app]
