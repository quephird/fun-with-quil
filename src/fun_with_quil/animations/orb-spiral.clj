(ns fun-with-quil.animations.orb-spiral
  (:use quil.core))

(def screen-w 800)
(def screen-h screen-w)

(defn setup []
  (smooth)
  (color-mode :hsb 1))

(defn draw []
  (let [num-circles  150  ; Needs to be even for perfect animation loop
        num-frames   (* num-circles 1.818)
        inner-r      (* screen-w 0.1)
        outer-r      (* screen-w 0.5)
        max-circle-r (* screen-w 0.05)
        t            (rem (map-range (dec (frame-count)) 0 num-frames 0 1) 1)]
    (background 0)
    (push-matrix)
    (translate (/ screen-w 2) (/ screen-h 2))

    (doseq [i (range num-circles)]
      (let [twisting-factor  16 ; This also needs to be even
            th (/ (* twisting-factor TWO-PI i) num-circles)
            tt (rem (+ (* 0.5 t) (/ i num-circles)) 1)
            r  (lerp inner-r outer-r tt)
            d' (map-range (cos (* TWO-PI tt)) 1 -1 0 1)
            d  (* max-circle-r (- (* 3 d' d') (* 2 d' d' d')))]
        (push-matrix)
        (rotate th)
        (fill t 255 255)
        (ellipse r 0 d d)
        (pop-matrix)))
    (pop-matrix)))

(sketch
  :title "orb spiral"
  :setup setup
  :draw draw
  :size [screen-w screen-h])
