(ns fun-with-quil.sketches.lines-and-crosses
  (:use quil.core))

(def screen-w 1920)
(def screen-h 1080)

(defn background-gradient [c1 c2 w h]
  (let [middle-c (map #(/ (+ %1 %2) 2) c1 c2)
        vertices [[middle-c [0 h]]
                  [c1 [w h]]
                  [middle-c [w 0]]
                  [c2 [0 0]]]]
    (begin-shape :quads)
    (doseq [[c [x y]] vertices]
      (apply fill c)
      (vertex x y))
    (end-shape)))

(defn cell [cell-w c]
  (let [w (* cell-w 1.1)
        half-w (/ w 2)
        possible-lines [[0 0 w w]
                        [0 half-w w half-w]
                        [0 w w 0]
                        [half-w 0 half-w w]]]
    (doseq [i (range 4)]
      (if (zero? (rand-int 2))
        (do
          (apply stroke c)
          (apply line (possible-lines i)))))))

(defn foreground-lines [c1 c2 w h]
  (let [cell-w 50
        rows (/ h cell-w)
        columns (/ w cell-w)]
    (stroke-weight 10)
    (stroke-cap :round)
    (doseq [_ (range rows)]
      (push-matrix)
      (doseq [_ (range columns)]
        (cell cell-w c2)
        (translate cell-w 0))
      (pop-matrix)
      (translate 0 cell-w))))

(defn setup []
  (smooth)
  (no-loop))

(defn draw []
  (background-gradient [255 127 0] [255 0 255] screen-w screen-h)
  (foreground-lines [0 255 0] [0 0 255] screen-w screen-h)
  (save "lines-and-crosses.png"))

(sketch
  :title "inspired by jennifer daniel"
  :setup setup
  :draw draw
  :renderer :p2d
  :size [screen-w screen-h])
