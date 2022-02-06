import os

import pytest
from rest_framework.test import RequestsClient

from imgscan import settings
from imgscan.models import DETECT_DEFAULT
from imgscan.serializers import OBJECTS_DEFAULT


MEDIA = 'http://testserver/media/imgscan'


@pytest.mark.django_db
def test_query_rocket():
    client = RequestsClient()

    filename = 'Serenity-Now-Featured-Image'
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

    response = client.get('http://testserver/images?objects=Canidae')
    assert response.status_code == 200
    data = response.json()
    assert len(data) == 0
    
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
    assert data['id'] == 2
    imgfile = data['imgfile']
    assert imgfile.startswith(F'{MEDIA}/{filename}')
    assert imgfile.endswith(filetype)
    assert data['detect'] == DETECT_DEFAULT
    x_objects = data['objects']
    assert len(x_objects) > len(OBJECTS_DEFAULT)
    for x_object in OBJECTS_DEFAULT:
        assert x_object in data['objects']
    # We are, I suppose, assuming that this images won't have the same
    # objects detected, but I think that is a safe bet for these two.
    key = x_objects[-1]

    response = client.get(F'http://testserver/images?objects={key}')
    assert response.status_code == 200
    data = response.json()
    assert len(data) == 1
    data = data[0]
    assert data['id'] == 2
    imgfile = data['imgfile']
    assert imgfile.startswith(F'{MEDIA}/{filename}')
    assert imgfile.endswith(filetype)
