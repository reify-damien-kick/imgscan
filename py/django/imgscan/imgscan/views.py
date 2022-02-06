import logging

from django.db import transaction
from rest_framework import parsers, renderers, status, viewsets
from rest_framework.permissions import AllowAny
from rest_framework.response import Response

from imgscan.core import update_image_labels
from imgscan.models import Image, ImgObject
from imgscan.serializers import ImageSerializer


logger = logging.getLogger(__name__)


class ImageViewSet(viewsets.ModelViewSet):
    permissions = (AllowAny,)    # No auth in spec. Maybe add later?
    queryset = Image.dbobjects.all()
    serializer_class = ImageSerializer
    renderer_classes = (renderers.JSONRenderer,)
    parser_classes = (parsers.MultiPartParser,)

    def perform_create(self, serializer):
        with transaction.atomic():
            image = serializer.save()
            maybe_update_image_labels(image)

    def retrieve(self, request, *args, **kwargs):
        with transaction.atomic():
            image = self.get_object()
            maybe_update_image_labels(image)
            serializer = self.get_serializer(image)
        return Response(serializer.data)

    def get_queryset(self):
        queryset = Image.dbobjects.all()

        if (imgobjects := self.request.query_params.get('objects')):
            for imgobject in imgobjects.split(','):
                queryset = queryset.filter(objects__label=imgobject)

        return queryset


def maybe_update_image_labels(image):
    try:
        if image.detect and image.scanned is None:
            update_image_labels(image)
    except Exception as exc:
        logger.exception(str(exc))
