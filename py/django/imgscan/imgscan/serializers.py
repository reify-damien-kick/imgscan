from rest_framework import serializers

from imgscan.models import Image, ImgObject

class ImgObjectSerializer(serializers.ModelSerializer):
    class Meta:
        model = ImgObject
        fields = ['label']


class ImageSerializer(serializers.ModelSerializer):
    objects = ImgObjectSerializer(
        many=True, required=False, allow_null=True)
    
    class Meta:
        model = Image
        fields = ['imgfile', 'detect', 'objects']
