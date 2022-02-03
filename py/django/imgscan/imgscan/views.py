from django.db import transaction
from rest_framework import status
from rest_framework import viewsets
from rest_framework.permissions import AllowAny
from rest_framework.response import Response

from imgscan.core import update_image_labels
from imgscan.models import Image
from imgscan.serializers import ImageSerializer


class ImageViewSet(viewsets.ModelViewSet):
    permissions = [AllowAny]    # No auth in spec. Maybe add later?
    queryset = Image.dbobjects.all()
    serializer_class = ImageSerializer

    def perform_create(self, serializer):
        image = serializer.save()
        maybe_update_image_labels(image)
    
    def retrieve(self, request, *args, **kwargs):
        image = self.get_object()
        maybe_update_image_labels(image)
        serializer = self.get_serializer(image)
        return Response(serializer.data)


def maybe_update_image_labels(image):
    print(F'image.detect = {image.detect}')
    print(F'image.scanned = {image.scanned}')
    with transaction.atomic():
        if image.detect and image.scanned is None:
            try:
                update_image_labels(image)
            except Exception:
                pass
    return image
