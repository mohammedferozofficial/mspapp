package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class StudyMaterialRepository(private val studyMaterialDao: StudyMaterialDao) {

    val allMaterials: Flow<List<StudyMaterial>> = studyMaterialDao.getAllMaterials()

    fun getMaterialById(id: Int): Flow<StudyMaterial?> {
        return studyMaterialDao.getMaterialById(id)
    }

    suspend fun insert(material: StudyMaterial): Long {
        return studyMaterialDao.insertMaterial(material)
    }

    suspend fun update(material: StudyMaterial) {
        studyMaterialDao.updateMaterial(material)
    }

    suspend fun delete(material: StudyMaterial) {
        studyMaterialDao.deleteMaterial(material)
    }

    suspend fun checkAndSeedDatabase() {
        val currentList = studyMaterialDao.getAllMaterials().first()
        if (currentList.isEmpty()) {
            studyMaterialDao.insertMaterials(getSeedMaterials())
        }
    }

    private fun getSeedMaterials(): List<StudyMaterial> {
        return listOf(
            StudyMaterial(
                title = "Introduction to Big-O Notation",
                subject = "Computer Science",
                type = "NOTES",
                content = "Big-O notation is used to describe the performance or complexity of an algorithm.\n\n### Core Concepts\n\n1. **Worst-Case Scenario**: Big-O characterizes the maximum time or space an algorithm takes.\n2. **Growth Rate**: It focuses on how runtime scales as input size (N) grows, ignoring constant factors.\n\n### Common Big-O Complexities\n\n- **O(1) - Constant Time**: Running time is independent of input size (e.g., accessing an array index).\n- **O(log N) - Logarithmic Time**: Decreases problem size by half at each step (e.g., Binary Search).\n- **O(N) - Linear Time**: Scales proportionally with input (e.g., Linear Search, Single Loop).\n- **O(N log N) - Linearithmic**: Common in efficient sorting algorithms (e.g., Merge Sort, Quick Sort).\n- **O(N²) - Quadratic Time**: Nested loops over the input (e.g., Bubble Sort, Insertion Sort).\n\n### Example Analysis\n\n```kotlin\nfun findMax(arr: IntArray): Int {\n    var max = arr[0] // O(1)\n    for (num in arr) { // O(N)\n        if (num > max) max = num\n    }\n    return max // O(1)\n}\n```\n\nTotal Time Complexity: **O(N)** because the loop dominates.",
                isFavorite = true,
                branch = "Computer Science",
                semester = "Semester 1",
                description = "A complete analysis of computational complexity, Big-O notation, and standard asymptotic bounds."
            ),
            StudyMaterial(
                title = "Laws of Thermodynamics",
                subject = "Physics",
                type = "NOTES",
                content = "Thermodynamics is the branch of physics dealing with heat, work, and temperature.\n\n### The Four Laws\n\n#### 0. Zeroth Law (Thermal Equilibrium)\nIf two thermodynamic systems are each in thermal equilibrium with a third system, then they are in thermal equilibrium with each other. This defines the concept of temperature.\n\n#### 1. First Law (Conservation of Energy)\nEnergy cannot be created or destroyed, only transformed from one form to another.\n\nFormula:\n`ΔU = Q - W`\n\nWhere:\n- `ΔU`: Change in internal energy\n- `Q`: Heat added to the system\n- `W`: Work done by the system\n\n#### 2. Second Law (Entropy)\nThe total entropy of an isolated system always increases over time. Heat cannot spontaneously flow from a colder body to a warmer body.\n\n#### 3. Third Law (Absolute Zero)\nAs the temperature of a system approaches absolute zero (0 Kelvin), the entropy of a system approaches a constant minimum value.",
                isFavorite = false,
                branch = "Mechanical Engineering",
                semester = "Semester 3",
                description = "Core principles of thermodynamics, covering absolute zero, entropy, and conservation of energy."
            ),
            StudyMaterial(
                title = "Calculus Cheat Sheet",
                subject = "Mathematics",
                type = "PDF",
                content = "This is a comprehensive study reference guide for Calculus. It covers limits, derivatives, integration rules, and core theorems.\n\n### 1. Fundamental Limits\n\n- `lim(x -> 0) [sin(x) / x] = 1`\n- `lim(x -> 0) [(1 + x)^(1/x)] = e`\n\n### 2. Standard Derivatives\n\n- **Power Rule**: `d/dx [x^n] = n * x^(n-1)`\n- **Product Rule**: `(uv)' = u'v + uv'`\n- **Quotient Rule**: `(u / v)' = (u'v - uv') / v²`\n- **Trigonometric derivatives**:\n  - `d/dx [sin x] = cos x`\n  - `d/dx [cos x] = -sin x`\n  - `d/dx [tan x] = sec² x`\n\n### 3. Key Integrals\n\n- `∫ x^n dx = (x^(n+1) / (n+1)) + C` (for n ≠ -1)\n- `∫ (1/x) dx = ln|x| + C`\n- `∫ e^x dx = e^x + C`\n- `∫ sin x dx = -cos x + C`\n\n### 4. Core Theorems\n\n- **Mean Value Theorem**: If f is continuous on [a,b] and differentiable on (a,b), there exists a point 'c' in (a,b) such that:\n  `f'(c) = (f(b) - f(a)) / (b - a)`",
                pdfUrl = "calculus_sheet",
                isFavorite = true,
                branch = "Computer Science",
                semester = "Semester 1",
                description = "A quick reference guide for limits, derivatives, integration rules, and core calculus theorems."
            ),
            StudyMaterial(
                title = "Organic Chemistry Formula Sheet",
                subject = "Chemistry",
                type = "PDF",
                content = "A cheat sheet detailing basic reaction mechanisms, naming conventions, and common organic structures.\n\n### Hydrocarbons\n\n1. **Alkanes** (C_n H_{2n+2}): Single bonds (e.g., Methane, Ethane).\n2. **Alkenes** (C_n H_{2n}): Double bonds (e.g., Ethene, Propene).\n3. **Alkynes** (C_n H_{2n-2}): Triple bonds (e.g., Ethyne).\n\n### Key Functional Groups\n\n- **Alcohol**: -OH group (suffix: -ol, e.g., Ethanol).\n- **Carboxylic Acid**: -COOH group (suffix: -oic acid, e.g., Ethanoic Acid).\n- **Aldehyde**: -CHO group (suffix: -al, e.g., Ethanal).\n- **Ketone**: -C=O group (suffix: -one, e.g., Propanone).\n- **Ether**: -O- group (e.g., Dimethyl ether).\n- **Ester**: -COOR group (suffix: -oate, e.g., Ethyl ethanoate).\n- **Famous Reactions**\n- **Nucleophilic Substitution (S_N1 and S_N2)**\n- **Electrophilic Addition**\n- **Esterification**: Reaction of alcohol and carboxylic acid to form ester + water.",
                pdfUrl = "organic_chem",
                isFavorite = false,
                branch = "Chemical Engineering",
                semester = "Semester 2",
                description = "Quick reference guide for organic compound naming, reaction mechanisms, and chemical structures."
            ),
            StudyMaterial(
                title = "Explain TCP vs UDP",
                subject = "Computer Science",
                type = "NOTES",
                content = "### Question\n\nWhat are the primary differences between TCP (Transmission Control Protocol) and UDP (User Datagram Protocol)?\n\n### Comprehensive Answer\n\n#### TCP\n- **Connection**: Connection-oriented (requires a 3-way handshake before sending data).\n- **Reliability**: Guaranteed delivery, retransmits lost packets, preserves order.\n- **Flow Control**: Implements sliding window for flow and congestion control.\n- **Speed**: Slower due to high overhead (20-60 byte header).\n- **Use Cases**: Web browsing (HTTP/S), Email (SMTP), File transfer (FTP).\n\n#### UDP\n- **Connection**: Connectionless (just sends packets directly).\n- **Reliability**: Unreliable, best-effort delivery, can arrive out of order, no retransmission.\n- **Flow Control**: None.\n- **Speed**: Very fast with minimal overhead (8 byte header).\n- **Use Cases**: Video streaming, Online gaming, DNS, VoIP.\n\n### The TCP 3-Way Handshake\n\n1. **SYN**: Client sends a SYN (synchronize) packet to server.\n2. **SYN-ACK**: Server replies with SYN-ACK (acknowledge/synchronize).\n3. **ACK**: Client replies with ACK (acknowledge). Connection established!",
                isFavorite = true,
                branch = "Computer Science",
                semester = "Semester 4",
                description = "Detailed breakdown of connection-oriented TCP vs best-effort UDP protocols and the 3-way handshake."
            ),
            StudyMaterial(
                title = "Explain Quantum Entanglement",
                subject = "Physics",
                type = "NOTES",
                content = "### Question\n\nWhat is Quantum Entanglement and why did Einstein call it 'spooky action at a distance'?\n\n### Comprehensive Answer\n\n**Quantum Entanglement** is a physical phenomenon that occurs when a pair or group of particles are generated, interact, or share spatial proximity in a way such that the quantum state of each particle cannot be described independently of the state of the others, even when the particles are separated by a large distance.\n\n### Core Properties\n\n1. **Superposition**: Particles exist in all possible states simultaneously until they are measured.\n2. **Instant Correlation**: Measuring particle A collapses its state and instantly determines the state of particle B, regardless of physical separation.\n3. **No Speed Limit**: The correlation happens instantly, faster than the speed of light.\n\n### Spooky Action at a Distance\n\nAlbert Einstein rejected the implication that information could be communicated faster than light speed, which seemed to violate Special Relativity. He believed there must be hidden variables ('hidden instructions') set from the beginning. However, countless experiments have proven quantum entanglement is real and indeed acts instantaneously, though it cannot be used to transmit actual human data/messages faster than light.",
                isFavorite = false,
                branch = "Electrical Engineering",
                semester = "Semester 3",
                description = "Introduction to wave function collapse, spooky action at a distance, and quantum superposition."
            ),
            StudyMaterial(
                title = "GATE CS 2023 - Q15: Page Faults (LRU)",
                subject = "Computer Science",
                type = "NOTES",
                content = "### Question\n\nConsider a virtual memory system with 3 page frames. The page reference string is:\n`1, 2, 3, 4, 1, 2, 5, 1, 2, 3, 4, 5`\n\nFind the number of page faults using the Least Recently Used (LRU) replacement policy.\n\n### Detailed Step-by-Step Solution\n\nWe have 3 initially empty page frames. Let's trace each reference:\n\n1. Ref **1**: Frame: `[1, _, _]` -> **Page Fault (1)**\n2. Ref **2**: Frame: `[1, 2, _]` -> **Page Fault (2)**\n3. Ref **3**: Frame: `[1, 2, 3]` -> **Page Fault (3)**\n4. Ref **4**: Frame: `[4, 2, 3]` -> **Page Fault (4)** (Replaces `1` as it was least recently used)\n5. Ref **1**: Frame: `[4, 1, 3]` -> **Page Fault (5)** (Replaces `2` as it was least recently used)\n6. Ref **2**: Frame: `[4, 1, 2]` -> **Page Fault (6)** (Replaces `3` as it was least recently used)\n7. Ref **5**: Frame: `[5, 1, 2]` -> **Page Fault (7)** (Replaces `4` as it was least recently used)\n8. Ref **1**: Frame: `[5, 1, 2]` -> **Page Hit** (1 is already in frame. LRU stack is now: `2, 5, 1`)\n9. Ref **2**: Frame: `[5, 1, 2]` -> **Page Hit** (2 is already in frame. LRU stack is now: `5, 1, 2`)\n10. Ref **3**: Frame: `[3, 1, 2]` -> **Page Fault (8)** (Replaces `5` as it was least recently used)\n11. Ref **4**: Frame: `[3, 4, 2]` -> **Page Fault (9)** (Replaces `1` as it was least recently used)\n12. Ref **5**: Frame: `[3, 4, 5]` -> **Page Fault (10)** (Replaces `2` as it was least recently used)\n\n### Final Answer\n\nTotal Page Faults = **10**",
                isFavorite = true,
                branch = "Computer Science",
                semester = "Semester 5",
                description = "Step-by-step solution to virtual memory page replacement policies under LRU."
            ),
            StudyMaterial(
                title = "CBSE Physics 2024 - Q8: Lenz's Law",
                subject = "Physics",
                type = "NOTES",
                content = "### Question\n\nState Lenz's Law of electromagnetic induction. Show that Lenz's Law is a consequence of the law of conservation of energy.\n\n### Detailed Step-by-Step Solution\n\n### Lenz's Law Statement\n\nLenz's Law states that the direction of an induced current in a conductor will be such that it opposes the change in magnetic flux that produced it.\n\nFormula:\n`E = -dΦ/dt`\n(where the negative sign indicates opposition to change)\n\n### Conservation of Energy Proof\n\n1. When the North pole of a bar magnet is pushed toward a coil, an electric current is induced in the coil.\n2. According to Lenz's law, the induced current creates a magnetic field with a North pole on the side facing the magnet, which *opposes* the incoming North pole.\n3. Because of this repulsive force, external mechanical work must be done to push the magnet closer.\n4. This mechanical work is directly converted into the electrical energy of the induced current.\n5. If the opposite were true (inducing a South pole), the magnet would be pulled in and accelerate indefinitely without work, creating infinite energy from nothing. This would violate the Law of Conservation of Energy.\n\nTherefore, Lenz's Law is a beautiful consequence of the **Law of Conservation of Energy**.",
                isFavorite = false,
                branch = "Electrical Engineering",
                semester = "Semester 2",
                description = "State Lenz's Law of induction and detailed proof of conservation of energy."
            )
        )
    }
}
