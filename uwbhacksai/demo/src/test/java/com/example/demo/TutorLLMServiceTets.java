package com.example.demo;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import com.example.demo.tutor.TutorLLMService;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {TutorLLMService.class, ExternalApiConfig.class})
class TutorLLMServiceTests {
    @Autowired
    private TutorLLMService instance;

    @Test
    public void testStartConversation() {

        String mockContent = " Potato plants are herbaceous perennials that grow about 60 centimetres (24 inches) high, depending on variety, with the leaves dying back after flowering, fruiting and tuber formation. The alternately arranged leaves have a petiole with six to eight symmetrical leaflets and one top, unpaired leaflet, which is 10 cm (3.9 in) to 30 cm (12 in) long and 5 cm (2.0 in) to 15 cm (5.9 in) wide. They present hairs or trichomes on their surface, to varying degrees depending on the cultivar.Potato plants bear white, pink, red, blue, or purple flowers with yellow stamens. Potatoes are mostly cross-pollinated by insects such as bumblebees, which carry pollen from other potato plants, though a substantial amount of self-fertilizing occurs as well.The plant develops tubers as a nutrient storage organ. Traditionally, it was thought that the tubers are roots because they are developed underground. In fact, they are stems that form from thickened rhizomes) at the tips of stolons. These stolons arise as branches from underground nodes.[20] On the surface of the tubers there are eyes, which act as sinks to protect the vegetative buds from which the stems originate. The eyes are arranged in helical form. In addition, the tubers have small holes that allow breathing, called lenticels. The lenticels are circular and their number varies depending of the size of the tuber and environmental conditions. Tubers form in response to decreasing day length, although this tendency has been minimized in commercial varieties.[21]After flowering, potato plants produce small green fruits that resemble green cherry tomatoes, each containing about 300 very small seeds.[22] Like all parts of the plant except the tubers, the fruit contain the toxic alkaloid solanine and are therefore unsuitable for consumption. All new potato varieties are grown from seeds, also called true potato seed, TPS or botanical seed to distinguish it from seed tubers.[23] New varieties grown from seed can be propagated vegetatively by planting tubers, pieces of tubers cut to include at least one or two eyes, or cuttings, a practice used in greenhouses for the production of healthy seed tubers. Plants propagated from tubers are clones of the parent, whereas those propagated from seed produce a range of different varieties.  ";
        String mockTutor = "";

        String topic = "potato";

        String aiResponse = instance.startConversation(mockContent, mockTutor);

        assertTrue(aiResponse.contains(topic), "Model should return a response on the topic. If the topic string isnt in the starting response, then this should fail");

    }
}
