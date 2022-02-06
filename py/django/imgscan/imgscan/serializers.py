from django.db import transaction
from rest_framework import serializers

from imgscan.models import (
    Image, ImgObject, DETECT_DEFAULT, LABEL_MAX_LENGTH)


OBJECTS_DEFAULT = ['image']


class ImageSerializer(serializers.ModelSerializer):
    class Meta:
        model = Image
        fields = ('id', 'imgfile', 'detect', 'objects')
        read_only_fields = ('scanned',)

    detect = serializers.BooleanField(default=DETECT_DEFAULT)

    objects = serializers.ListSerializer(
        child=serializers.CharField(max_length=LABEL_MAX_LENGTH),
        default=OBJECTS_DEFAULT)

    def create(self, validated_data):
        labels = validated_data.pop('objects', [])
        with transaction.atomic():
            image = Image.dbobjects.create(**validated_data)
            for label in labels:
                imgobject = ImgObject.objects.get_or_create(label=label)
                imgobject = imgobject[0]
                image.objects.add(imgobject)
            image.save()
            return image
