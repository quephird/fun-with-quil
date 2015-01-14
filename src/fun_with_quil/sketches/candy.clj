(ns fun-with-quil.sketches.candy
  (:require [quil.core :as q :include-macros :true]
            [quil.middleware :as m]))

(def existing-spheres (atom []))

(defn- rand-color []
  (vec (repeatedly 3 (fn [] (rand-int 255)))))

(defn- vary-color [c]
  (let [variance 30
        delta-c (vec (repeatedly 3 (fn [] (- (rand-int (* 2 variance)) variance))))]
    (map + c delta-c)))

(defn- calc-color [w h x y c1 c2]
  (let [delta-x1 x
        delta-y1 y
        d1-squared (+ (* delta-x1 delta-x1) (* delta-y1 delta-y1))
        weighted-c1 (map #(* d1-squared %) c1)
        delta-x2 (- w x)
        delta-y2 (- h y)
        d2-squared (+ (* delta-x2 delta-x2) (* delta-y2 delta-y2))
        weighted-c2 (map #(* d2-squared %) c2)
        seed-color (map #(/ % (+ d1-squared d2-squared)) (map + weighted-c1 weighted-c2))]
    (vary-color seed-color)))

(defn- add-sphere! [x y r]
  (swap! existing-spheres conj [x y r]))

(defn- draw-sphere [x y r c]
  (q/push-matrix)
  (q/translate x y)
  (apply q/fill c)
  (q/sphere r)
  (q/pop-matrix))

(defn- spheres-intersect? [[x1 y1 r1] [x2 y2 r2]]
  (let [delta-x (- x1 x2)
        delta-y (- y1 y2)
        sum-of-radii (+ r1 r2)
        distance-squared (+ (* delta-x delta-x) (* delta-y delta-y))]
    (> (* sum-of-radii sum-of-radii) distance-squared)))

(defn- intersects-any-sphere? [existing-spheres new-sphere]
  (if (empty? existing-spheres)
    false
    (some true? (map (fn [existing-sphere] (spheres-intersect? existing-sphere new-sphere)) existing-spheres))))

(defn setup []
  (let [w (q/width)
        h (q/height)]
    (q/background 0)
    (q/smooth)
    (q/no-stroke)
    (q/no-loop)
    {:w w :h h}))

(defn draw [{w :w h :h}]
    (q/directional-light 255 255 225
                         1000 1000 -500)
    (q/ambient-light 100 100 100)
    (q/ambient 100 100 100)
    (q/shininess 2.0)

    ; This is to prevent distortion of the spheres.
;    (q/ortho (* w -0.45) (* w 0.45) (* h 0.45) (* h -0.45) 0 1000)
    (q/translate (* 0.5 w) (* 0.5 h))
    (q/ortho 0 w h 0 -1000 1000)
    (doseq [_ (range 500)]
      ; Randomize x,y coordinates of each sphere
      (let [x (q/random (* -0.5 w) (* 0.5 w))
            y (q/random (* -0.5 h) (* 0.5 h))

             ; Randomize radius of each candy
            r (+ 1 (rand-int 25))
            c (calc-color w h x y [255 0 0] [0 0 255])]
        ; This is a fairly hackish approach since I just throw away undesired choices;
        ; ideally I ought to generate usable coordinates each time but I don't know
        ; how to implement that at this time.
        (if (not (intersects-any-sphere? @existing-spheres [x y r]))
          (do
            (draw-sphere x y r c)
            (add-sphere! x y r)))))
    (q/save "candy.png"))

(q/sketch
  :title "candy"
  :size [1920 1080]
  :setup setup
  :draw draw
  :renderer :p3d
  :middleware [m/fun-mode])
