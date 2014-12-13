(ns fun-with-quil.animations.orbits
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]))

(defn make-orbit [w h cs]
  (let [absolute-θ (q/random 360)
        max-r (* 0.3 (min w h))
        r (q/random max-r)
        x (+ (* 0.5 w) (* r (q/cos absolute-θ)))
        y (+ (* 0.5 h) (* r (q/sin absolute-θ)))
        max-d (q/random (/ max-r 6) (/ max-r 3))
        dθ (q/random 180)
        c (q/unhex (cs (rand-int (count cs))))]
    [x y max-d dθ c]))

(defn init-orbits [n w h cs]
  (into []
    (for [_ (range n)]
      (make-orbit w h cs))))

(defn setup []
  (let [w (q/width)
        h (q/height)
        cs ["ffc25d7a"
            "ffad5f75"
            "ff8a606e"
            "ffdbbfad"
            "ffffecc5"]]
    (q/smooth)
    (q/no-stroke)
    (q/fill 255)
    (init-orbits 50 w h cs)))

(defn draw [state]
  (let [fc (q/frame-count)]
    (q/fill 0 5)
    (q/rect 0 0 800 800)
    (doseq [[x y max-d dθ c] state]
      (q/push-matrix)
      (q/translate x y)
      (let [min-d (* 0.6 max-d)
            d (+ min-d (* 0.5 (- max-d min-d) (q/sin (* 3 (q/radians (+ fc dθ))))))]
        (q/fill c)
        (q/ellipse (* 100 (q/cos (* 3 (q/radians (+ fc dθ)))))
                   (* 100 (q/sin (* 3 (q/radians (+ fc dθ)))))
                   d d))
      (q/pop-matrix))))

(q/defsketch orbits
  :title "orbits"
  :setup setup
  :draw draw
  :middleware  [m/fun-mode]
  :size [800 800])
