package com.isen.gomez_sanchez.androiderestaurant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Space
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.isen.gomez_sanchez.androiderestaurant.basket.Basket
import com.isen.gomez_sanchez.androiderestaurant.basket.BasketActivity
import com.isen.gomez_sanchez.androiderestaurant.network.Dish
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import kotlin.math.max


class DetailActivity : ComponentActivity() {
    @OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dish = intent.getSerializableExtra(DISH_EXTRA_KEY) as? Dish
        setContent {
            val context = LocalContext.current
            val count = remember {
                mutableIntStateOf(1)
            }
            val ingredientList = dish?.ingredients?.map { it.name } ?: emptyList()
            val ingredient = ingredientList.joinToString(", ")
            val pagerState = rememberPagerState(pageCount = {
                dish?.images?.count() ?: 0
            })
            var showButton = true

            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                TopAppBar({
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        IconButton(onClick = {
                            val intent = Intent(context, HomeActivity::class.java)
                            context.startActivity(intent)

                        },
                            modifier = Modifier.background(Color.Transparent)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_launcher),
                                contentDescription = null,
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(dish?.name ?: "")
                    }

                })
                HorizontalPager(state = pagerState) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(dish?.images?.get(it))
                            .build(),
                        null,
                        placeholder = painterResource(R.drawable.ic_launcher_foreground),
                        error = painterResource(R.drawable.ic_launcher_foreground),
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier
                            .height(200.dp)
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                }
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Ingrédients :")
                    ingredientList.forEach { ingredient ->
                        Text("- $ingredient")
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(Modifier.weight(1f))
                    OutlinedButton(onClick = {
                        count.value = max(1, count.value - 1)
                    }) {
                        Text("-")
                    }
                    Text(count.value.toString())
                    OutlinedButton(onClick = {
                        count.value = count.value + 1
                    }) {
                        Text("+")
                    }
                    Spacer(Modifier.weight(1f))
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Button(onClick = {
                        if (dish != null) {
                            Basket.current(context).add(dish, count.value, context)
                        }
                    }) {
                        Text("Commander")
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Button(onClick = {

                        val intent = Intent(context, BasketActivity::class.java)
                        context.startActivity(intent)
                    }) {
                        Text("Voir mon panier")
                    }

                }
            }
        }
    }

    companion object {
        val DISH_EXTRA_KEY = "DISH_EXTRA_KEY"
    }
}