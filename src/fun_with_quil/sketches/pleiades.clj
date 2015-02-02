(ns fun-with-quil.sketches.pleiades
  (:use [quil.core :as q])
  (:import [processing.core PConstants]))

; TODO: Need to apply blur to entire background; stars look too fake here
(defn generate-background-stars [w h]
  (q/background 0)
  (dotimes [_  1000]
    (let [r (q/random 5)]
      (q/fill 255 255 255)
      (q/ellipse (q/random w) (q/random h) r r))))

; TODO: Need to blur blue rings or starlight together instead of independently;
; forces a completely different implementation of this routine
(defn- generate-star [x y r]
  (let [gc-width 500
        gc-center (/ gc-width 2)
        gc (create-graphics gc-width gc-width)]
    (doto gc
      (.beginDraw)
      (.noStroke)
      (.fill 0 0 127)
      (.ellipse gc-center gc-center (* r 1.5) (* r 1.5))
      (.filter PConstants/BLUR 15)
      (.fill 255 255 255)
      (.ellipse gc-center gc-center (* r 0.5) (* r 0.5))
      (.filter PConstants/BLUR 4)
      (.stroke 255 255 255)
      (.noFill)
      (.ellipse gc-center gc-center (* r 1.1) (* r 1.1))
      (.line (- gc-center r) gc-center (+ gc-center r) gc-center)
      (.line gc-center (- gc-center r) gc-center (+ gc-center r))
      (.filter PConstants/BLUR 2)
      (.endDraw))
    (image gc (- x gc-center) (- y gc-center))))

(defn- generate-pleides []
  (doseq [star [[500 600 100]
                [500 550 40]
                [800 575 150]
                [950 700 100]
                [1025 315 30]
                [1040 300 30]
                [1050 425 100]
                [1150 350 50]
                [1200 575 100]
                [1210 475 40]]]
    (apply generate-star star)))

(defn setup []
  (q/no-stroke)
  (q/no-loop))

(defn draw []
  (let [w (q/width)
        h (q/height)]
    (generate-background-stars w h)
    (generate-pleides)
    (q/save "pleiades.png")))

(q/defsketch pleiades
  :size [1920 1080]
  :title "pleiades"
  :setup setup
  :draw draw)
