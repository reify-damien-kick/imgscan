from django.core.management.base import BaseCommand

from imgscan.core import safe_update_images


class Command(BaseCommand):
    help = 'Uses google-cloud-vision to scan for objects in images'

    def handle(self, *args, **kwargs):
        safe_update_images()
  
