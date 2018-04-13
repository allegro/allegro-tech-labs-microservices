request = function()
   path = "/offers/" .. math.random(1, 100000)
   return wrk.format(nil, path)
end