import os

import pytest
from rest_framework.test import RequestsClient

from imgscan import settings
from imgscan.models import DETECT_DEFAULT
from imgscan.serializers import OBJECTS_DEFAULT


MEDIA = 'http://testserver/media/imgscan'


@pytest.mark.django_db
def test_post_rocket():
    client = RequestsClient()

    filename = 'Rocket-Raccoon-2'
    filetype = '.jpg'
    fullname = F'{filename}{filetype}'
    fullpath = os.path.join(
        settings.BASE_DIR, 'tests', fullname)
    with open(fullpath, 'rb') as x_file:
        data = {'imgfile': (fullname, x_file, 'image/jpeg')}
        response = client.post('http://testserver/images', files=data)
    assert response.status_code == 201
    data = response.json()
    assert data['id'] == 1
    imgfile = data['imgfile']
    assert imgfile.startswith(F'{MEDIA}/{filename}')
    assert imgfile.endswith(filetype)
    assert data['detect'] == DETECT_DEFAULT
    for x_object in OBJECTS_DEFAULT:
        assert x_object in data['objects']
    objects_1 = data['objects']

    with open(fullpath, 'rb') as x_file:
        data = {'imgfile': (fullname, x_file, 'image/jpeg')}
        response = client.post('http://testserver/images', files=data)
    assert response.status_code == 201
    data = response.json()
    assert data['id'] == 2
    imgfile = data['imgfile']
    assert imgfile.startswith(F'{MEDIA}/{filename}')
    assert imgfile.endswith(filetype)
    assert data['detect'] == DETECT_DEFAULT
    for x_object in OBJECTS_DEFAULT:
        assert x_object in data['objects']
    objects_2 = data['objects']

    assert objects_1 == objects_2

    response = client.get('http://testserver/images/1')
    assert response.status_code == 200
    data = response.json()
    assert data['objects'] == objects_1

    response = client.get('http://testserver/images/2')
    assert response.status_code == 200
    data = response.json()
    assert data['objects'] == objects_2
