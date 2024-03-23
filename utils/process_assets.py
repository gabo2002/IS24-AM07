#!/usr/bin/python3

#
# Codex Naturalis - Final Assignment for the Software Engineering Course
# Copyright (C) 2024 Andrea Biasion Somaschini, Roberto Alessandro Bertolini, Omar Chaabani, Gabriele Corti
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# Please note that the GNU General Public License applies only to the
# files that contain this license header. Other files within the project, such
# as assets and images, are property of the original owners and may be
# subject to different copyright terms.
#
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>.
#

# this file should be placed in the same directory with the provided game assets

from dataclasses import dataclass
from enum import Enum
import json
import os
import shutil
import pdf2image
from psd_tools import PSDImage
from PIL import Image, ImageDraw, ImageChops
from typing import Tuple

IMAGE_X = 792
IMAGE_Y = 544

shutil.rmtree('output', ignore_errors=True)
os.mkdir('output')

filenames = ['CODEX_cards_gold_back.pdf', 'CODEX_cards_gold_front.pdf']

# create a mask to have rounded corners on the cards
card_mask = Image.new('RGBA', (IMAGE_X, IMAGE_Y))
draw = ImageDraw.Draw(card_mask)
draw.rounded_rectangle([0, 0, IMAGE_X, IMAGE_Y], 24, fill=(255, 255, 255, 255))

for filename in filenames:
    images = pdf2image.convert_from_path(
        filename, thread_count=16, fmt='png', dpi=300, use_pdftocairo=True, use_cropbox=True
    )
    name = 'front' if 'front' in filename else 'back'
    for i, image in enumerate(images):
        image = image.crop((87 - 24, 87 - 24, 831 + 24, 583 + 24))
        assert image.size == (IMAGE_X, IMAGE_Y)
        image = image.convert('RGBA')
        image = ImageChops.multiply(image, card_mask)
        image.save(f'output/{name}_{i}.png')

# convert table
table = 'PLATEAU-SCORE-IMP.pdf'
image = pdf2image.convert_from_path(table, thread_count=16, fmt='png', dpi=300, use_pdftocairo=True)[0]
image = image.crop((118, 118, 1299, 2480))
image.save('output/plateau_score_imp.png')

# get all files ending in .psd
psd_files = [f for f in os.listdir('.') if f.endswith('.psd')]

for psd_file in psd_files:
    # open the file using PIL
    image = PSDImage.open(psd_file)
    image = image.composite()

    # convert to RGB
    image = image.convert('RGBA')

    # delete white background
    datas = image.getdata()
    newData = []
    for item in datas:
        if item[0] == 255 and item[1] == 255 and item[2] == 255:
            newData.append((255, 255, 255, 0))
        else:
            newData.append(item)
    image.putdata(newData)

    # export the image as a PNG
    image.save(f'output/{psd_file.removesuffix('.psd')}.png')

# parse the various images and build the resources definition file
class Symbol(Enum):
    NONE = 0
    BLANK = 1
    RED = 2
    GREEN = 3
    BLUE = 4
    PURPLE = 5
    SCROLL = 6
    FLASK = 7
    FEATHER = 8
    CORNER = 9
    EMPTY = 10
    STARTER = 11

class CardType(Enum):
    BASE = 0
    GOLD = 1
    STARTER = 2

@dataclass(frozen=True)
class Side:
    id: int
    front: bool

    topLeft: Symbol
    topRight: Symbol
    bottomLeft: Symbol
    bottomRight: Symbol
    center: list[Symbol]

    symbolRequirements: list[Symbol]
    score: int
    scoreMultiplier: Symbol

    def isResource(self) -> bool:
        return len(self.symbolRequirements) == 0 and self.scoreMultiplier == Symbol.NONE and len(self.center) == 1 and self.center[0] != Symbol.NONE
    
    def isGold(self) -> bool:
        return len(self.symbolRequirements) > 0 and len(self.center) == 1 and self.center[0] != Symbol.NONE
    
    def isBack(self) -> bool:
        return len(self.symbolRequirements) == 0 and self.score == 0 and len(self.center) == 1 and all(s != Symbol.NONE for s in [self.topLeft, self.topRight, self.bottomLeft, self.bottomRight, self.center[0]])

@dataclass(frozen=True)
class Card:
    id: int
    front: Side
    back: Side

def average_color(image: Image) -> tuple[int, int, int]:
    pixels = list(image.getdata())
    r = g = b = 0
    for pixel in pixels:
        r += pixel[0]
        g += pixel[1]
        b += pixel[2]
    return (r // len(pixels), g // len(pixels), b // len(pixels))

def distance_n3(a, b) -> int:
    return (a[0] - b[0]) ** 2 + (a[1] - b[1]) ** 2 + (a[2] - b[2]) ** 2

RED_PIXEL = (192, 141, 111)
GREEN_PIXEL = (133, 162, 107)
BLUE_PIXEL = (153, 167, 158)
PURPLE_PIXEL = (144, 110, 130)
BLANK_PIXEL = (221, 215, 160)
RED_NONE = (137, 25, 33)
GREEN_NONE = (49, 123, 64)
BLUE_NONE = (60, 110, 157)
BLUE_NONE_2 = (97, 177, 183)
BLUE_NONE_3 = (44, 65, 146)
PURPLE_NONE = (97, 36, 114)
FLASK_PIXEL = (173, 160, 109)
SCROLL_PIXEL = (182, 169, 113)
FEATHER_PIXEL = (198, 189, 136)

def find_symbol(image: Image) -> Symbol:
    pixel = average_color(image)

    if distance_n3(pixel, FLASK_PIXEL) < 100:
        return Symbol.FLASK
    if distance_n3(pixel, SCROLL_PIXEL) < 100:
        return Symbol.SCROLL
    if distance_n3(pixel, RED_PIXEL) < 1000:
        return Symbol.RED
    if distance_n3(pixel, GREEN_PIXEL) < 1000:
        return Symbol.GREEN
    if distance_n3(pixel, BLUE_PIXEL) < 1000:
        return Symbol.BLUE
    if distance_n3(pixel, PURPLE_PIXEL) < 1000:
        return Symbol.PURPLE
    if distance_n3(pixel, FEATHER_PIXEL) < 1000:
        return Symbol.FEATHER
    if distance_n3(pixel, BLANK_PIXEL) < 1000:
        return Symbol.BLANK
    if distance_n3(pixel, RED_NONE) < 1000:
        return Symbol.NONE
    if distance_n3(pixel, GREEN_NONE) < 2000:
        return Symbol.NONE
    if distance_n3(pixel, BLUE_NONE_2) < 1000:
        return Symbol.NONE
    if distance_n3(pixel, BLUE_NONE_3) < 1000:
        return Symbol.NONE
    if distance_n3(pixel, BLUE_NONE) < 2000:
        return Symbol.NONE
    if distance_n3(pixel, PURPLE_NONE) < 2000:
        return Symbol.NONE
    
    image.show()
    print(pixel)
    assert False

RED_CENTER = (176, 34, 39)
GREEN_CENTER = (59, 142, 74)
GREEN_CENTER_2 = (36, 101, 52)
BLUE_CENTER = (58, 129, 156)
BLUE_CENTER_2 = (81, 183, 173)
PURPLE_CENTER = (96, 34, 111)

def find_symbol_center(image: Image) -> Symbol:
    pixel = average_color(image)
    if distance_n3(pixel, RED_CENTER) < 1000:
        return Symbol.RED
    if distance_n3(pixel, GREEN_CENTER_2) < 1000:
        return Symbol.GREEN
    if distance_n3(pixel, GREEN_CENTER) < 2000:
        return Symbol.GREEN
    if distance_n3(pixel, BLUE_CENTER_2) < 1000:
        return Symbol.BLUE
    if distance_n3(pixel, BLUE_CENTER) < 2000:
        return Symbol.BLUE
    if distance_n3(pixel, PURPLE_CENTER) < 1000:
        return Symbol.PURPLE
        
    image.show()
    print(pixel)
    assert False

def parse_score(image: Image) -> Tuple[int, Symbol]:
    if image.getpixel((351, 56)) == (53, 31, 22, 255):
        score = 1
    elif image.getpixel((340, 58)) == (53, 31, 22, 255):
        score = 2
    else:
        image.show()
        raise ValueError('Unknown score')
    
    if image.getpixel((459, 56)) == (53, 31, 22, 255) and image.getpixel((458, 49)) != (183, 154, 60, 255):
        return score, Symbol.FEATHER
    elif image.getpixel((458, 49)) == (183, 154, 60, 255):
        return score, Symbol.FLASK
    elif image.getpixel((437, 62)) == (53, 31, 22, 255):
        return score, Symbol.SCROLL
    elif image.getpixel((480, 54)) == (174, 144, 56, 255):
        return score, Symbol.CORNER
    else:
        image.show()
        raise ValueError('Unknown multiplier')

def parse_score_no_multiplier(image: Image) -> int:
    if image.getpixel((397, 62)) == (53, 31, 22, 255):
        return 1
    elif image.getpixel((405, 59)) == (53, 31, 22, 255):
        return 3
    elif image.getpixel((387, 70)) == (53, 31, 22, 255):
        return 5
    else:
        image.show()
        raise ValueError('Unknown score')

def parse_front(id: int, image: Image) -> Side:
    topleft_subcrop = image.crop((50, 50, 170, 200))
    topright_subcrop = image.crop((IMAGE_X - 170, 50, IMAGE_X - 50, 200))
    center_subcrop = image.crop((IMAGE_X // 2 - 50, IMAGE_Y // 2 - 50, IMAGE_X // 2 + 50, IMAGE_Y // 2 + 50))
    bottomleft_subcrop = image.crop((50, IMAGE_Y - 200, 170, IMAGE_Y - 50))
    bottomright_subcrop = image.crop((IMAGE_X - 170, IMAGE_Y - 200, IMAGE_X - 50, IMAGE_Y - 50))

    topleft_symbol = find_symbol(topleft_subcrop)
    topright_symbol = find_symbol(topright_subcrop)
    center_symbol = find_symbol_center(center_subcrop)
    bottomleft_symbol = find_symbol(bottomleft_subcrop)
    bottomright_symbol = find_symbol(bottomright_subcrop)

    assert center_symbol in [Symbol.RED, Symbol.GREEN, Symbol.BLUE, Symbol.PURPLE]

    if has_no_score(image):
        score = 0
        score_multiplier = Symbol.NONE
    elif has_multiplier(image):
        score, score_multiplier = parse_score(image)
    else:
        score = parse_score_no_multiplier(image)
        score_multiplier = Symbol.NONE

    side = Side(
        id=id,
        front=True,
        topLeft=topleft_symbol,
        topRight=topright_symbol,
        bottomLeft=bottomleft_symbol,
        bottomRight=bottomright_symbol,
        center=[center_symbol],
        symbolRequirements=parse_requirements(id, image),
        score=score,
        scoreMultiplier=score_multiplier
    )

    return side

BACK_RED_CENTER = (181, 102, 86)
BACK_GREEN_CENTER = (83, 133, 77)
BACK_BLUE_CENTER = (110, 139, 157)
BACK_PURPLE_CENTER = (119, 73, 123)

def find_symbol_center_back(image: Image) -> Symbol:
    pixel = average_color(image)
    if distance_n3(pixel, BACK_RED_CENTER) < 1000:
        return Symbol.RED
    if distance_n3(pixel, BACK_GREEN_CENTER) < 1000:
        return Symbol.GREEN
    if distance_n3(pixel, BACK_BLUE_CENTER) < 1000:
        return Symbol.BLUE
    if distance_n3(pixel, BACK_PURPLE_CENTER) < 1000:
        return Symbol.PURPLE
        
    image.show()
    print(pixel)
    assert False

reqs = {
    (239, 45, 50, 255): Symbol.RED,
    (54, 172, 156, 255): Symbol.BLUE,
    (44, 133, 58, 255): Symbol.GREEN,
    (142, 26, 133, 255): Symbol.PURPLE,
}

def has_multiplier(image: Image) -> bool:
    return image.getpixel((286, 18)) == (53, 31, 22, 255)

def has_no_score(image: Image) -> bool:
    return image.getpixel((394, 18)) != (53, 31, 22, 255)

def parse_requirements(id: int, image: Image) -> list[Symbol]:
    if image.getpixel((288, 517)) == (53, 31, 22, 255):
        # 3 requirements
        req1 = image.getpixel((347, 479))
        req2 = image.getpixel((398, 479))
        req3 = image.getpixel((449, 479))

        return [reqs[req1], reqs[req2], reqs[req3]]
    elif image.getpixel((258, 521)) == (53, 31, 22, 255):
        # 4 requirements
        req1 = image.getpixel((318, 479))
        req2 = image.getpixel((369, 479))
        req3 = image.getpixel((420, 479))
        req4 = image.getpixel((471, 479))

        return [reqs[req1], reqs[req2], reqs[req3], reqs[req4]]
    elif image.getpixel((240, 523)) == (53, 31, 22, 255):
        # 5 requirements
        req1 = image.getpixel((300, 479))
        req2 = image.getpixel((351, 479))
        req3 = image.getpixel((402, 479))
        req4 = image.getpixel((453, 479))
        req5 = image.getpixel((504, 479))

        return [reqs[req1], reqs[req2], reqs[req3], reqs[req4], reqs[req5]]
    
    return []

def parse_back(id: int, image: Image) -> Side:
    topleft_subcrop = image.crop((50, 50, 170, 200))
    topright_subcrop = image.crop((IMAGE_X - 170, 50, IMAGE_X - 50, 200))
    center_subcrop = image.crop((IMAGE_X // 2 - 50, IMAGE_Y // 2 - 50, IMAGE_X // 2 + 50, IMAGE_Y // 2 + 50))
    bottomleft_subcrop = image.crop((50, IMAGE_Y - 200, 170, IMAGE_Y - 50))
    bottomright_subcrop = image.crop((IMAGE_X - 170, IMAGE_Y - 200, IMAGE_X - 50, IMAGE_Y - 50))

    topleft_symbol = find_symbol(topleft_subcrop)
    topright_symbol = find_symbol(topright_subcrop)
    center_symbol = find_symbol_center_back(center_subcrop)
    bottomleft_symbol = find_symbol(bottomleft_subcrop)
    bottomright_symbol = find_symbol(bottomright_subcrop)

    assert topleft_symbol == Symbol.BLANK
    assert topright_symbol == Symbol.BLANK
    assert bottomleft_symbol == Symbol.BLANK
    assert bottomright_symbol == Symbol.BLANK
    assert center_symbol in [Symbol.RED, Symbol.GREEN, Symbol.BLUE, Symbol.PURPLE]

    side = Side(
        id=id,
        front=False,
        topLeft=topleft_symbol,
        topRight=topright_symbol,
        bottomLeft=bottomleft_symbol,
        bottomRight=bottomright_symbol,
        center=[center_symbol],
        symbolRequirements=[],
        score=0,
        scoreMultiplier=Symbol.NONE
    )

    return side

cards = []

for i in range(80):
    front = Image.open(f'output/front_{i}.png')
    back = Image.open(f'output/back_{i}.png')

    front = parse_front(i, front)
    back = parse_back(i, back)

    assert back.isBack()
    if i < 40:
        assert front.isResource()
    else:
        assert front.isGold()

    cards.append(Card(i, front, back))

def side_field_representation(side: Side):
    return f'''
    {{
        "corners": {{
            "centerX": 0,
            "centerY": 0,
            "data": [
                "{side.topLeft.name.upper()}",
                "{side.topRight.name.upper()}",
                "{side.bottomLeft.name.upper()}",
                "{side.bottomRight.name.upper()}"
            ],
            "emptyValue": "EMPTY",
            "maxX": 1,
            "maxY": 1,
            "minX": 0,
            "minY": 0,
            "sizeX": 2,
            "sizeY": 2
        }}
    }}
    '''

def eliminate_non_resources(res: dict):
    for symbol in [Symbol.EMPTY, Symbol.NONE, Symbol.BLANK, Symbol.CORNER, Symbol.STARTER]:
        if symbol in res:
            del res[symbol]

def side_resources(side: Side):
    resources = {}
    for symbol in [side.topLeft, side.topRight, side.bottomLeft, side.bottomRight]:
        if symbol in resources:
            resources[symbol] += 1
        else:
            resources[symbol] = 1
    if not side.front:
        if side.center[0] in resources:
            resources[side.center[0]] += 1
        else:
            resources[side.center[0]] = 1

    eliminate_non_resources(resources)

    return f'''
    {{
        "resources": {{
            {", ".join(f'"{k.name.upper()}": {v}' for k, v in resources.items())}
        }}
    }}
    '''

def side_requirements(side: Side):
    requirements = {}
    for symbol in side.symbolRequirements:
        if symbol in requirements:
            requirements[symbol] += 1
        else:
            requirements[symbol] = 1

    return f'''
    {{
        "resources": {{
            {", ".join(f'"{k.name.upper()}": {v}' for k, v in requirements.items())}
        }}
    }}
    '''

def serialize_side_back(side: Side):
    return f'''
    {{
        "class": "SideBack",
        "id": {side.id},
        "color": "{side.center[0].name.upper()}",
        "fieldRepresentation": {side_field_representation(side)},
        "resources": {side_resources(side)}
    }}
    '''

def serialize_side_front_gold(side: Side):
    return f'''
    {{
        "class": "SideFrontGold",
        "associatedScore": {side.score},
        "color": "{side.center[0].name.upper()}",
        "id": {side.id},
        "fieldRepresentation": {side_field_representation(side)},
        "resources": {side_resources(side)},
        "multiplier": "{side.scoreMultiplier.name.upper()}",
        "requirements": {side_requirements(side)}
    }}
    '''

def serialize_side_front_res(side: Side):
    return f'''
    {{
        "class": "SideFrontRes",
        "color": "{side.center[0].name.upper()}",
        "id": {side.id},
        "fieldRepresentation": {side_field_representation(side)},
        "resources": {side_resources(side)}
    }}
    '''

def serialize_side(side: Side):
    if not side.front:
        return serialize_side_back(side)
    elif side.isGold():
        return serialize_side_front_gold(side)
    else:
        return serialize_side_front_res(side)
    
def serialize_card(card: Card):
    return f'''
    {{
        "back": {serialize_side(card.back)},
        "front": {serialize_side(card.front)}
    }}
    '''

cards_json = '[\n' + ',\n'.join(serialize_card(card) for card in cards) + '\n]'

cards_json = json.dumps(json.loads(cards_json), indent=4)

with open('cards.json', 'w') as f:
    f.write(cards_json)

# validate each card
# for card in cards[40:]:
#     Image.open(f'output/front_{card.id}.png').show('Front')
#     Image.open(f'output/back_{card.id}.png').show('Back')
#     print(json.dumps(json.loads(serialize_card(card)), indent=4))
#     input()
