import os

import pytest
from rest_framework.test import RequestsClient

from imgscan import settings
from imgscan.models import DETECT_DEFAULT
from imgscan.serializers import OBJECTS_DEFAULT


MEDIA = 'http://testserver/media/imgscan'


@pytest.mark.django_db
def test_get_empty():
    client = RequestsClient()
    response = client.get('http://testserver/images')
    assert response.status_code == 200
    data = response.json()
    assert type(data) is list
    assert len(data) == 0


@pytest.mark.django_db
def test_post_oddities():
    client = RequestsClient()

    data = {'imgfile': ('filename.jpg', b'', 'image/jpeg')}
    response = client.post('http://testserver/images', files=data)
    assert response.status_code == 400

    data = {'imgfile': ('filename.jpg', b'0', 'image/jpeg')}
    response = client.post('http://testserver/images', files=data)
    assert response.status_code == 201
    data = response.json()
    assert data['id'] == 1
    imgfile = data['imgfile']
    assert imgfile.startswith(F'{MEDIA}/filename')
    assert imgfile.endswith('jpg')
    assert 'scanned' in data
    assert data['detect'] == DETECT_DEFAULT
    assert data['objects'] == OBJECTS_DEFAULT
