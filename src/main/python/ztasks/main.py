# [START gae_python37_app]
import os
import urllib.request
import json

from flask import Flask
from google.cloud import datastore

TOODLEDO_CLIENT_ID = os.environ.get('TOODLEDO_CLIENT_ID')
TOODLEDO_CLIENT_SECRET = os.environ.get('TOODLEDO_CLIENT_SECRET')
TOODLEDO_PASSWORD = os.environ.get('TOODLEDO_PASSWORD')

# If `entrypoint` is not defined in app.yaml, App Engine will look for an app
# called `app` in `main.py`.
app = Flask(__name__)
ds = datastore.Client()


@app.route('/')
def hello():
    contents = urllib.request.urlopen("http://api.toodledo.com/3/tasks/get.php?access_token=" +
                                      ds_get('access') + "&fields=repeat&comp=0").read()
    tasks = json.loads(contents)
    to_print = ""
    for task in tasks:
        if 'repeat' in task and task['repeat'] is not "":
            to_print += task['title'] + '<br>'
    return to_print


def ds_get(key):
    query = ds.query(kind='tokens')
    query.add_filter('key', '=', key)
    result = list(query.fetch())
    if result:
        return result[0]['value']
    else:
        return ""


def ds_put(key, value):
    entity = datastore.Entity(ds.key('tokens'))
    entity.update({
        'key': key,
        'value': value
    })
    ds.put(entity)


if __name__ == '__main__':
    # This is used when running locally only. When deploying to Google App
    # Engine, a webserver process such as Gunicorn will serve the app. This
    # can be configured by adding an `entrypoint` to app.yaml.
    app.run(host='127.0.0.1', port=8080, debug=True)
# [END gae_python37_app]
