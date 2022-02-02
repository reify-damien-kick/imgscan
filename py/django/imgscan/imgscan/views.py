from rest_framework import viewsets
from rest_framework.permissions import AllowAny

from imgscan.models import Image
from imgscan.serializers import ImageSerializer


class ImageViewSet(viewsets.ModelViewSet):
    permissions = [AllowAny]    # No auth in spec
    queryset = Image.objects.all()
    serializer_class = ImageSerializer
