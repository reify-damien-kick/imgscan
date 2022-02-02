from django.db import transaction
from rest_framework import serializers

from imgscan.models import Image, ImgObject

class ImgObjectSerializer(serializers.ModelSerializer):
    class Meta:
        model = ImgObject
        fields = ['label']


class ImageSerializer(serializers.ModelSerializer):
    class Meta:
        model = Image
        fields = ['id', 'imgfile', 'detect', 'objects']

    objects = ImgObjectSerializer(
        many=True, required=False, allow_null=True,
        default=[{'label': 'image'}])

    def create(self, validata):
        labels = validata.pop('objects')
        with transaction.atomic():
            image = Image.dbobjects.create(**validata)
            for label in labels:
                imgobject = ImgObject.objects.get_or_create(**label)[0]
                image.objects.add(imgobject)
            image.save()
            return image
                
