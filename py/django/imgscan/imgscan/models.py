from django.db import models


DETECT_DEFAULT = True
LABEL_MAX_LENGTH = 256


def imgpath(__, filename):
    return F'imgscan/{filename}'


class Image(models.Model):
    # The spec calls for a field named 'objects'; rename the default
    # manager to avoid colliding.
    dbobjects = models.Manager()

    id = models.BigAutoField(primary_key=True)
    imgfile = models.FileField(upload_to=imgpath, unique=True)
    detect = models.BooleanField(default=DETECT_DEFAULT)
    scanned = models.DateTimeField(null=True)

    # https://amir.rachum.com/blog/2013/06/15/
    # ... a-case-for-a-onetomany-relationship-in-django/
    objects = models.ManyToManyField('ImgObject')


class ImgObject(models.Model):
    id = models.BigAutoField(primary_key=True)
    label = models.CharField(max_length=LABEL_MAX_LENGTH, unique=True)

    def __str__(self):
        return self.label
