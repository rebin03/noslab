# Create a new simulator
set ns [new Simulator]

# Set the routing protocol to Distance Vector
$ns rtproto DV

# Open a trace file for writing
set nf [open dv1.tr w]
$ns trace-all $nf

# Open a nam trace file for writing
set nr [open dv2.nam w]
$ns namtrace-all $nr

# Define a procedure to finish the simulation
proc finish {} {
    global ns nf nr
    $ns flush-trace
    close $nf
    close $nr
    exec nam dv2.nam
    exit 0
}

# Create 12 nodes
for {set i 0} {$i<12} {incr i} {
    set n$i [$ns node]
}

# Create duplex links between nodes
$ns duplex-link $n0 $n1 1Mb 10ms DropTail
$ns duplex-link $n1 $n2 1Mb 10ms DropTail
$ns duplex-link $n2 $n3 1Mb 10ms DropTail
$ns duplex-link $n3 $n4 1Mb 10ms DropTail
$ns duplex-link $n4 $n5 1Mb 10ms DropTail
$ns duplex-link $n5 $n6 1Mb 10ms DropTail
$ns duplex-link $n6 $n7 1Mb 10ms DropTail
$ns duplex-link $n7 $n8 1Mb 10ms DropTail
$ns duplex-link $n8 $n0 1Mb 10ms DropTail
$ns duplex-link $n0 $n9 1Mb 10ms DropTail
$ns duplex-link $n1 $n10 1Mb 10ms DropTail
$ns duplex-link $n9 $n11 1Mb 10ms DropTail
$ns duplex-link $n10 $n11 1Mb 10ms DropTail
$ns duplex-link $n11 $n5 1Mb 10ms DropTail

# Create UDP agent and CBR application for node 0
set udp0 [new Agent/UDP]
$ns attach-agent $n0 $udp0
set cbr0 [new Application/Traffic/CBR]
$cbr0 attach-agent $udp0
$cbr0 set packetSize_ 500
$cbr0 set interval_ 0.005

# Create Null agent for node 5
set null0 [new Agent/Null]
$ns attach-agent $n5 $null0

# Create UDP agent and CBR application for node 1
set udp1 [new Agent/UDP]
$ns attach-agent $n1 $udp1
set cbr1 [new Application/Traffic/CBR]
$cbr1 attach-agent $udp1
$cbr1 set packetSize_ 500
$cbr1 set interval_ 0.005

# Create Null agent for node 5
set null1 [new Agent/Null]
$ns attach-agent $n5 $null1

# Connect agents and null agents
$ns connect $udp0 $null0
$ns connect $udp1 $null1

# Set routing model events
$ns rtmodel-at 10.0 down $n11 $n5
$ns rtmodel-at 30.0 up $n11 $n5
$ns rtmodel-at 15.0 down $n7 $n6
$ns rtmodel-at 20.0 up $n7 $n6

# Schedule CBR application start and stop events
$ns at 0.1 "$cbr1 start"
$ns at 0.2 "$cbr0 start"
$ns at 45.0 "$cbr1 stop"
$ns at 45.1 "$cbr0 stop"

# Schedule simulation finish event
$ns at 50.0 "finish"

# Run the simulation
$ns run