from rest_framework import serializers

from imgscan.models import Image


class ImageSerializer(serializers.ModelSerializer):
    # The spec calls for a field named 'objects' but this collides
    # with a Python field of the same name in the model. We're
    # providing an alias here
    objects = serializers.SlugRelatedField(
        many=True, slug_field='imgobjects', queryset=Image.objects.all())
    
    class Meta:
        model = Image
        fields = ['imgfile', 'detect']
